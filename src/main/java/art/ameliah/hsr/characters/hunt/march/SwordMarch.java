package art.ameliah.hsr.characters.hunt.march;

import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.battleLogic.log.lines.character.DoMove;
import art.ameliah.hsr.battleLogic.log.lines.character.ExtraHits;
import art.ameliah.hsr.battleLogic.log.lines.entity.GainCharge;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysBasicGoal;
import art.ameliah.hsr.characters.goal.shared.turn.SkillFirstTurnGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostAllyAttack;
import art.ameliah.hsr.events.character.PostBasic;
import art.ameliah.hsr.events.character.PreAllyAttack;
import art.ameliah.hsr.events.character.PreBasic;
import art.ameliah.hsr.events.character.PreUltimate;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.metrics.BoolMetric;
import art.ameliah.hsr.metrics.CounterMetric;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;
import lombok.Getter;

import java.util.List;
import java.util.Random;

public class SwordMarch extends AbstractCharacter<SwordMarch> implements SkillFirstTurnGoal.FirstTurnTracked {
    public static final String NAME = "Sword March";
    public static final int CHARGE_THRESHOLD = 7;

    protected CounterMetric<Integer> ultEnhancedEBA = this.metricRegistry.register(CounterMetric.newIntegerCounter("EHB boosted by ult"));
    protected CounterMetric<Integer> extraHits = this.metricRegistry.register(CounterMetric.newIntegerCounter("Extra hits during EHB"));
    @Getter
    protected CounterMetric<Integer> chargeCount = this.metricRegistry.register(CounterMetric.newIntegerCounter("Charge count"));
    protected BoolMetric hasUltEnhancement = this.metricRegistry.register("Has ult enchantment", BoolMetric.class);

    public AbstractCharacter<?> master;
    private Random fuaRng;
    private boolean isEnhanced;
    private boolean FUAReady = true;

    public SwordMarch() {
        super(NAME, 1058, 564, 441, 102, 80, ElementType.IMAGINARY, 110, 75, Path.HUNT);

        this.addPower(new TracePower()
                .setStat(PowerStat.ATK_PERCENT, 28)
                .setStat(PowerStat.CRIT_DAMAGE, 24)
                .setStat(PowerStat.DEF_PERCENT, 12.5f));
        this.hasAttackingUltimate = true;

        this.registerGoal(0, new SkillFirstTurnGoal<>(this));
        this.registerGoal(10, new AlwaysBasicGoal<>(this));
        this.registerGoal(0, new AlwaysUltGoal<>(this));
        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));
    }

    // Assuming always dps
    private void masterEffect(AttackLogic attack, AbstractEnemy target) {
        if (master != null) {
            boolean ignore = target.hasWeakness(this.elementType) || target.hasWeakness(master.elementType);
            attack.hit(this, target, 0.22f, MultiplierStat.ATK, 0, this.master.elementType, ignore);
        }
    }

    @Override
    public void useSkill() {
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            if (character.isDPS) {
                master = character;
                AbstractPower masterPower = new MarchMasterPower();
                getBattle().IncreaseSpeed(master, masterPower);
                getBattle().IncreaseSpeed(this, PermPower.create(PowerStat.SPEED_PERCENT, 10, "March Speed Boost"));
                break;
            }
        }
    }

    @Override
    public void useBasic() {
        this.startAttack().handle(DamageType.BASIC, dh -> dh.logic(this.getTarget(MoveType.BASIC), (e, al) -> {
            al.hit(e, 1.1f, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            this.masterEffect(al, e);
        })).afterAttackHook(() -> this.gainCharge(1)).execute();
    }

    @Override
    protected void basicSequence() {
        if (!this.isEnhanced) {
            super.basicSequence();
            return;
        }

        this.eventBus.fire(new PreBasic());
        this.useEnhancedBasicAttack();
        this.eventBus.fire(new PostBasic());
    }

    public void useEnhancedBasicAttack() {
        this.actionMetric.record(MoveType.ENHANCED_BASIC);
        getBattle().addToLog(new DoMove(this, MoveType.ENHANCED_BASIC));
        increaseEnergy(30, EBA_ENERGY_GAIN);

        this.startAttack().handle(DamageType.BASIC, dh -> {
            dh.logic(this.getTarget(MoveType.ENHANCED_BASIC), (enemy, al) -> {
                int initialHits = 3;
                int numExtraHits = 0;
                int procChance = 60;

                PermPower ultCritDmgBuff = PermPower.create(PowerStat.CRIT_DAMAGE, 50, "March Ult Crit Dmg Buff");
                PermPower ebaDamageBonus = PermPower.create(PowerStat.DAMAGE_BONUS, 88, "March Enhanced Basic Damage Bonus");

                this.addPower(ebaDamageBonus);
                if (this.hasUltEnhancement.value()) {
                    this.ultEnhancedEBA.increment();

                    initialHits += 2;
                    procChance = 80;
                    this.addPower(ultCritDmgBuff);
                }
                for (int i = 0; i < 3; i++) {
                    double roll = getBattle().getProcChanceRng().nextDouble() * 100 + 1;
                    if (roll < procChance) {
                        numExtraHits++;
                    } else {
                        break;
                    }
                }
                this.extraHits.increase(numExtraHits);

                getBattle().addToLog(new ExtraHits(this, numExtraHits));
                for (int i = 0; i < initialHits + numExtraHits; i++) {
                    al.hit(enemy, 0.88f, TOUGHNESS_DAMAGE_HALF_UNIT);
                    this.masterEffect(al, enemy);
                }
                if (this.hasUltEnhancement.value()) {
                    this.hasUltEnhancement.flip();
                    this.removePower(ultCritDmgBuff);
                }
                this.removePower(ebaDamageBonus);
                this.isEnhanced = false;
            });
        }).afterAttackHook(this::addEHBBuffToMaster).execute();
    }

    private void addEHBBuffToMaster() {
        this.master.addPower(TempPower.create(PowerStat.CRIT_DAMAGE, 60, 2, "Enhanced Basic Master Buff"));
    }

    public void useFollowUp(AbstractEnemy enemy) {
        if (FUAReady) {
            FUAReady = false;
            this.actionMetric.record(MoveType.FOLLOW_UP);
            getBattle().addToLog(new DoMove(this, MoveType.FOLLOW_UP));

            this.startAttack().handle(DamageType.FOLLOW_UP, dh -> {
                AbstractEnemy target = enemy.isDead() ? getBattle().getRandomEnemy() : enemy;
                dh.logic(target, al -> {
                    al.hit(target, 0.6f, TOUGHNESS_DAMAGE_SINGLE_UNIT);
                    this.masterEffect(al, target);
                });
            }).afterAttackHook(() -> {
                this.increaseEnergy(5, FUA_ENERGY_GAIN);
                this.gainCharge(1);
            }).execute();
        }
    }

    public void useUltimate() {
        this.hasUltEnhancement.set(true);
        this.doAttack(DamageType.ULTIMATE,
                dh -> dh.logic(this.getTarget(MoveType.ULTIMATE),
                        (e, al) -> al.hit(e, 2.59f, TOUGHNESS_DAMAGE_THREE_UNITs)));
    }

    public void onTurnStart() {
        FUAReady = true;
        increaseEnergy(5, "from E4");
        if (this.currentEnergy.get() >= this.ultCost) {
            tryUltimate();
        }
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent e) {
        this.fuaRng = new Random(getBattle().getSeed());
        getBattle().AdvanceEntity(this, 25);
    }

    @Override
    public void useTechnique() {
        gainCharge(3);
        increaseEnergy(30, TECHNIQUE_ENERGY_GAIN);
    }

    public void gainCharge(int amount) {
        int currentCharge = this.chargeCount.get();
        this.chargeCount.increase(amount);

        getBattle().addToLog(new GainCharge(this, amount, currentCharge, this.chargeCount.get()));
        if (this.chargeCount.get() >= CHARGE_THRESHOLD) {
            this.chargeCount.decrease(CHARGE_THRESHOLD);
            getBattle().AdvanceEntity(this, 100);
            this.isEnhanced = true;
        }
    }

    @Override
    public boolean isFirstTurn() {
        return firstMove;
    }

    @Override
    public void setFirstTurn(boolean firstTurn) {
        firstMove = firstTurn;
    }

    public class MarchMasterPower extends PermPower {
        public MarchMasterPower() {
            this.setName(this.getClass().getSimpleName());
            this.setStat(PowerStat.SPEED_PERCENT, 10.8f);
        }

        @Subscribe
        public void beforeAttack(PreAllyAttack event) {
            SwordMarch.this.gainCharge(1);
        }

        @Subscribe
        public void afterAttack(PostAllyAttack event) {
            var attack = event.getAttack();
            if (attack.getTypes().contains(DamageType.BASIC) || attack.getTypes().contains(DamageType.SKILL)) {
                List<AbstractEnemy> nonDead = attack.getTargets().stream().filter(e -> !e.isDead()).toList();
                AbstractEnemy enemy;
                if (nonDead.isEmpty()) {
                    enemy = getBattle().getRandomEnemy();
                } else {
                    enemy = nonDead.get(SwordMarch.this.fuaRng.nextInt(nonDead.size()));
                }

                SwordMarch.this.useFollowUp(enemy);
            }
        }

        @Subscribe
        public void onUseUltimate(PreUltimate event) {
            if (!master.hasAttackingUltimate) {
                SwordMarch.this.gainCharge(1);
            }
        }
    }
}
