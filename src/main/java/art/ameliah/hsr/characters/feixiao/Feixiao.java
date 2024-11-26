package art.ameliah.hsr.characters.feixiao;

import art.ameliah.hsr.battleLogic.combat.AttackLogic;
import art.ameliah.hsr.battleLogic.log.lines.character.DoMove;
import art.ameliah.hsr.battleLogic.log.lines.character.GainEnergy;
import art.ameliah.hsr.battleLogic.log.lines.entity.GainCharge;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysSkillGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.characters.goal.shared.ult.BroynaRobinUltGoal;
import art.ameliah.hsr.characters.goal.shared.ult.DontUltMissingDebuffGoal;
import art.ameliah.hsr.characters.goal.shared.ult.DontUltMissingPowerGoal;
import art.ameliah.hsr.characters.goal.shared.ult.UltAtEndOfBattle;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.metrics.CounterMetric;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;

import java.util.List;
import java.util.Random;

public class Feixiao extends AbstractCharacter<Feixiao> {

    public static final String NAME = "Feixiao";
    public static final int STACK_THRESHOLD = 2;

    protected CounterMetric<Float> gainedStacks = metricRegistry.register(CounterMetric.newFloatCounter("Gained stacks", "Amount of Talent Stacks gained"));
    protected CounterMetric<Float> wastedStacks = metricRegistry.register(CounterMetric.newFloatCounter("Wasted stacks", "Amount of overcapped Stacks"));
    protected CounterMetric<Integer> stacks = metricRegistry.register(CounterMetric.newIntegerCounter("Stacks", "Left over stacks"));

    final PermPower ultBreakEffBuff = PermPower.create(PowerStat.WEAKNESS_BREAK_EFF, 100, "Fei Ult Break Eff Buff");
    private Random fuaRng;
    private boolean FUAReady = true;

    public Feixiao() {
        super(NAME, 1048, 602, 388, 112, 80, ElementType.WIND, 12, 75, Path.HUNT);

        this.usesEnergy = false;
        this.currentEnergy.set(0f);
        this.ultCost = 6;
        this.isDPS = true;
        this.hasAttackingUltimate = true;

        this.addPower(new TracePower()
                .setStat(PowerStat.ATK_PERCENT, 28)
                .setStat(PowerStat.CRIT_CHANCE, 12)
                .setStat(PowerStat.DEF_PERCENT, 12.5f));

        this.registerGoal(0, new FeixiaoBronyaTurnGoal(this));
        this.registerGoal(10, new AlwaysSkillGoal<>(this, 1));

        this.registerGoal(0, new UltAtEndOfBattle<>(this));
        this.registerGoal(10, new BroynaRobinUltGoal<>(this));
        this.registerGoal(20, DontUltMissingPowerGoal.robin(this));
        this.registerGoal(30, DontUltMissingPowerGoal.sparkle(this));
        this.registerGoal(40, DontUltMissingPowerGoal.bronya(this));
        this.registerGoal(50, DontUltMissingPowerGoal.ruanmei(this));
        this.registerGoal(60, DontUltMissingPowerGoal.hanya(this));
        this.registerGoal(70, DontUltMissingDebuffGoal.pela(this));
        this.registerGoal(100, new AlwaysUltGoal<>(this));

        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));
    }


    public void increaseStack(int amount) {
        int initialStack = this.stacks.get();
        this.stacks.increase(amount);
        getBattle().addToLog(new GainCharge(this, amount, initialStack, this.stacks.get(), "Stack"));
        if (this.stacks.get() >= STACK_THRESHOLD) {
            int energyGain = this.stacks.get() / STACK_THRESHOLD; // WUT?
            gainStackEnergy(energyGain);
        }
    }

    public void gainStackEnergy(float energyGain) {
        this.gainedStacks.increase(energyGain);
        float initialEnergy = this.currentEnergy.get();

        this.currentEnergy.increase(energyGain);
        if (this.currentEnergy.get() > this.maxEnergy) {
            this.wastedStacks.increase(this.currentEnergy.get() - this.maxEnergy);
            this.currentEnergy.set(this.maxEnergy);
        }
        this.stacks.set(this.stacks.get() % STACK_THRESHOLD); // Again wut?
        getBattle().addToLog(new GainEnergy(this, initialEnergy, this.currentEnergy.get(), energyGain, TALENT_ENERGY_GAIN));
    }

    public void useSkill() {
        this.addPower(TempPower.create(PowerStat.ATK_PERCENT, 48, 3, "Fei Atk Bonus"));

        this.doAttack(DamageType.SKILL, MoveType.SKILL, (e, al) -> {
            float totalMult = 2.0f;
            al.hit(e, totalMult * 0.33f);
            al.hit(e, totalMult * 0.33f);
            al.hit(e, totalMult * 0.33f, TOUGHNESS_DAMAGE_TWO_UNITS);
            this.useFollowUp(e);
        });
    }

    public void useBasic() {
        this.doAttack(DamageType.BASIC, MoveType.BASIC, (e, al) -> al.hit(e, 1, TOUGHNESS_DAMAGE_SINGLE_UNIT));
    }

    public void useFollowUp(final AbstractEnemy target) {
        this.actionMetric.record(MoveType.FOLLOW_UP);
        getBattle().addToLog(new DoMove(this, MoveType.FOLLOW_UP));

        this.doAttack(DamageType.FOLLOW_UP, dh -> {
            this.addPower(TempPower.create(PowerStat.DAMAGE_BONUS, 60, 2, "Fei Damage Bonus"));
            AbstractEnemy enemy = target.isDead() ? getBattle().getRandomEnemy() : target;

            dh.logic(enemy, al -> al.hit(enemy, 1.1f, TOUGHNESS_DAMAGE_HALF_UNIT));
        });
    }

    public void useUltimate() {
        this.startAttack()
                .handle(dh -> {
                    dh.addTypes(DamageType.ULTIMATE, DamageType.FOLLOW_UP);

                    dh.logic(this.getTarget(MoveType.ULTIMATE), (e, al) -> {
                        this.addPower(ultBreakEffBuff);

                        int numHits = 6;
                        float totalMult = 0.9f;
                        for (int i = 0; i < numHits; i++) {
                            if (e.isWeaknessBroken()) {
                                al.hit(e, totalMult * 0.1f);
                                al.hit(e, totalMult * 0.9f, TOUGHNESS_DAMAGE_HALF_UNIT);
                            } else {
                                al.hit(e, totalMult, TOUGHNESS_DAMAGE_HALF_UNIT);
                            }
                        }
                    });
                }).afterAttackHook(() -> this.removePower(ultBreakEffBuff)).execute();
    }

    public void onTurnStart() {
        if (this.currentEnergy.get() >= ultCost) {
            tryUltimate(); // check for ultimate activation at start of turn as well
        }
        if (FUAReady) {
            increaseStack(1);
        }
        FUAReady = true;
    }

    public void onCombatStart() {
        this.fuaRng = new Random(getBattle().getSeed());
        gainStackEnergy(3);
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            AbstractPower feiPower = new FeiTalentPower();
            character.addPower(feiPower);
        }
        addPower(new FeiCritDmgPower());
    }

    public void useTechnique() {
        if (getBattle().usedEntryTechnique()) {
            return;
        } else {
            getBattle().setUsedEntryTechnique(true);
        }
        // This assumes a fix multiplier on her technique
        this.doAttack(dh -> dh.logic(getBattle().getEnemies(), (e, al) -> al.hit(e, 2, TOUGHNESS_DAMAGE_SINGLE_UNIT)));
        this.gainStackEnergy(1);
    }

    private static class FeiCritDmgPower extends AbstractPower {
        public FeiCritDmgPower() {
            this.setName(this.getClass().getSimpleName());
            this.lastsForever = true;
        }

        @Override
        public float getConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.FOLLOW_UP) {
                    return 36;
                }
            }
            return 0;
        }
    }

    private class FeiTalentPower extends AbstractPower {
        public FeiTalentPower() {
            this.setName(this.getClass().getSimpleName());
            this.lastsForever = true;
        }

        @Override
        public void afterAttack(AttackLogic attack) {
            if (!Feixiao.this.hasPower(ultBreakEffBuff.getName())) {
                Feixiao.this.increaseStack(1);
            }

            if (!(attack.getSource() instanceof Feixiao)) {
                if (FUAReady) {
                    FUAReady = false;

                    List<AbstractEnemy> alive = attack.getTargets().stream().filter(e -> !e.isDead()).toList();
                    AbstractEnemy enemy;
                    if (alive.isEmpty()) {
                        enemy = getBattle().getRandomEnemy();
                    } else {
                        enemy = alive.get(Feixiao.this.fuaRng.nextInt(alive.size()));
                    }
                    Feixiao.this.useFollowUp(enemy);
                }
            }
        }
    }
}
