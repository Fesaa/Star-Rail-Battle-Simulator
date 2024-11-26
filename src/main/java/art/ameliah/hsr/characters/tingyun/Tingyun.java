package art.ameliah.hsr.characters.tingyun;

import art.ameliah.hsr.battleLogic.combat.AttackLogic;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.metrics.CounterMetric;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;
import art.ameliah.hsr.utils.Randf;
import lombok.Getter;

import java.util.List;

public class Tingyun extends AbstractCharacter<Tingyun> {

    public static final String NAME = "Tingyun";

    protected CounterMetric<Integer> talentProcs = metricRegistry.register(CounterMetric.newIntegerCounter("tingyun-talent-procs", "Talent Extra Damage Procs"));

    @Getter
    private AbstractCharacter<?> benefactor;

    public Tingyun() {
        super(NAME, 847, 529, 397, 112, 80, ElementType.LIGHTNING, 130, 100, Path.HARMONY);

        this.addPower(new TracePower()
                .setStat(PowerStat.ATK_PERCENT, 28)
                .setStat(PowerStat.DEF_PERCENT, 22.5f)
                .setStat(PowerStat.LIGHTNING_DMG_BOOST, 8));

        this.registerGoal(0, new AlwaysUltGoal<>(this));
        this.registerGoal(0, new TingyunTurnGoal(this));
        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));
    }

    public void useSkill() {
        TingyunSkillPower skillPower = new TingyunSkillPower();
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            if (character.isDPS) {
                benefactor = character;
                character.addPower(skillPower);
                break;
            }
        }

        TempPower speedPower = TempPower.create(PowerStat.SPEED_PERCENT, 20, 1, "Tingyun Skill Speed Power");
        speedPower.justApplied = true;
        getBattle().IncreaseSpeed(this, speedPower);
    }

    public void useBasic() {
        this.doAttack(DamageType.BASIC, MoveType.BASIC, (e, al) -> al.hit(e, 1.1f, TOUGHNESS_DAMAGE_SINGLE_UNIT));
    }

    public void useUltimate() {
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            if (character.isDPS && character.getCurrentEnergy().get() < character.maxEnergy) {
                character.increaseEnergy(60, false, "from Tingyun Ult");
                character.addPower(TempPower.create(PowerStat.DAMAGE_BONUS, 56, 2, "Tingyun Ult Damage Bonus"));
                break;
            }
        }
    }

    public void onTurnStart() {
        increaseEnergy(5, TRACE_ENERGY_GAIN);
        tryUltimate();
    }

    public void useTechnique() {
        increaseEnergy(maxEnergy, false, TECHNIQUE_ENERGY_GAIN);
    }

    public void onCombatStart() {
        addPower(new TingyunBonusBasicDamagePower());
    }

    private static class TingyunBonusBasicDamagePower extends AbstractPower {
        public TingyunBonusBasicDamagePower() {
            this.setName(this.getClass().getSimpleName());
            this.lastsForever = true;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.BASIC) {
                    return 40;
                }
            }
            return 0;
        }
    }

    private class TingyunSkillPower extends TempPower {
        public TingyunSkillPower() {
            super(3);

            this.setName(this.getClass().getSimpleName());
            this.setStat(PowerStat.ATK_PERCENT, 55);
        }

        public void beforeAttack(AttackLogic attack) {
            Tingyun.this.talentProcs.increment();
            AbstractEnemy target = Randf.rand(attack.getTargets(), getBattle().getGetRandomEnemyRng());
            attack.hit(attack.getSource(), target, 0.64f, MultiplierStat.ATK, 0, ElementType.LIGHTNING, false, List.of());
        }

        public void onUseUltimate() {
            if (benefactor != null) {
                getBattle().IncreaseSpeed(benefactor, TempPower.create(PowerStat.SPEED_PERCENT, 20, 1, "Tingyun E1 Speed Power"));
            }
        }

        public void onRemove() {
            benefactor = null;
        }
    }
}
