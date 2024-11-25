package art.ameliah.hsr.characters.pela;

import art.ameliah.hsr.battleLogic.combat.AttackLogic;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysBasicGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.characters.goal.shared.turn.SkillFirstTurnGoal;
import art.ameliah.hsr.characters.goal.shared.turn.UseExcessSkillPointsGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;

import java.util.List;

public class Pela extends AbstractCharacter<Pela> implements SkillFirstTurnGoal.FirstTurnTracked {

    public static final String NAME = "Pela";
    public static final String ULT_DEBUFF_NAME = "Pela Ult Def Reduction";

    public Pela() {
        super(NAME, 988, 547, 463, 105, 80, ElementType.ICE, 110, 100, Path.NIHILITY);

        this.addPower(new TracePower()
                .setStat(PowerStat.ATK_PERCENT, 18)
                .setStat(PowerStat.ICE_DMG_BOOST, 22.4f)
                .setStat(PowerStat.EFFECT_HIT, 10));
        this.hasAttackingUltimate = true;

        this.registerGoal(0, new SkillFirstTurnGoal<>(this));
        this.registerGoal(10, new UseExcessSkillPointsGoal<>(this));
        this.registerGoal(20, new AlwaysBasicGoal<>(this));

        this.registerGoal(0, new AlwaysUltGoal<>(this));

        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));
    }

    // TODO: Ice res down
    @Override
    public void useSkill() {
        this.doAttack(DamageType.SKILL, dh -> dh.logic(this.getTarget(MoveType.SKILL), (e, al) -> {
            al.hit(e, 2.31f, TOUGHNESS_DAMAGE_TWO_UNITS);
        }));
    }

    @Override
    public void useBasic() {
        this.doAttack(DamageType.BASIC, dh -> dh.logic(this.getTarget(MoveType.SKILL), (e, al) -> {
            al.hit(e, 1.1f, TOUGHNESS_DAMAGE_SINGLE_UNIT);
        }));
    }

    @Override
    public void useUltimate() {
        this.doAttack(DamageType.ULTIMATE, dh -> dh.logic(getBattle().getEnemies(), (e, al) -> {
            al.hit(e, 1.08f, TOUGHNESS_DAMAGE_TWO_UNITS);
            for (AbstractEnemy enemy : e) {
                enemy.addPower(TempPower.createDebuff(PowerStat.DEFENSE_REDUCTION, 42, 2, ULT_DEBUFF_NAME));
            }
        }));
    }

    @Override
    public void useTechnique() {
        if (getBattle().usedEntryTechnique()) {
            return;
        } else {
            getBattle().setUsedEntryTechnique(true);
        }

        this.startAttack().handle(dh -> dh.logic(getBattle().getRandomEnemy(), (e, al) -> {
            al.hit(e, 0.8f, TOUGHNESS_DAMAGE_TWO_UNITS);
        })).afterAttackHook(() -> {
            for (AbstractEnemy enemy : getBattle().getEnemies()) {
                enemy.addPower(TempPower.createDebuff(PowerStat.DEFENSE_REDUCTION, 20, 2, "Pela Technique Def Reduction"));
            }
        }).execute();


    }

    public void onCombatStart() {
        addPower(new PelaTalentPower());
        addPower(new PelaBonusDamageAgainstDebuffPower());
    }

    @Override
    public boolean isFirstTurn() {
        return firstMove;
    }

    @Override
    public void setFirstTurn(boolean firstTurn) {
        this.firstMove = firstTurn;
    }

    private static class PelaBonusDamageAgainstDebuffPower extends AbstractPower {
        public PelaBonusDamageAgainstDebuffPower() {
            this.setName(this.getClass().getSimpleName());
            this.lastsForever = true;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            for (AbstractPower power : enemy.powerList) {
                if (power.type == PowerType.DEBUFF) {
                    return 20;
                }
            }
            return 0;
        }
    }

    private class PelaTalentPower extends AbstractPower {
        public PelaTalentPower() {
            this.setName(this.getClass().getSimpleName());
            this.lastsForever = true;
        }

        @Override
        public void beforeAttack(AttackLogic attack) {
            for (AbstractEnemy enemy : attack.getTargets()) {
                for (AbstractPower power : enemy.powerList) {
                    if (power.type == PowerType.DEBUFF) {
                        increaseEnergy(11, TALENT_ENERGY_GAIN);
                        break;
                    }
                }
            }
        }
    }
}
