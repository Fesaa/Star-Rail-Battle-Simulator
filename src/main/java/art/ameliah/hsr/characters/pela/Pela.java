package art.ameliah.hsr.characters.pela;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.Path;
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
    }

    // TODO: Ice res down
    @Override
    public void useSkill() {
        AbstractEnemy target = getBattle().getEnemyWithHighestHP();
        this.startAttack()
                .hitEnemy(target, 2.31f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_TWO_UNITS, DamageType.SKILL)
                .execute();
    }

    @Override
    public void useBasic() {
        this.startAttack()
                .hitEnemy(getBattle().getEnemyWithHighestHP(), 1.1f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.BASIC)
                .execute();
    }

    @Override
    public void useUltimate() {
        this.startAttack()
                .hitEnemies(getBattle().getEnemies(), 1.08f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_TWO_UNITS, DamageType.ULTIMATE)
                .execute();

        for (AbstractEnemy enemy : getBattle().getEnemies()) {
            TempPower exposed = TempPower.create(PowerStat.DEFENSE_REDUCTION, 42, 2, ULT_DEBUFF_NAME);
            exposed.type = AbstractPower.PowerType.DEBUFF;
            enemy.addPower(exposed);
        }
    }

    @Override
    public void useTechnique() {
        if (getBattle().usedEntryTechnique()) {
            return;
        } else {
            getBattle().setUsedEntryTechnique(true);
        }

        this.startAttack()
                .hitEnemy(getBattle().getRandomEnemy(), 0.8f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_TWO_UNITS)
                .execute();

        for (AbstractEnemy enemy : getBattle().getEnemies()) {
            TempPower techniqueExposed = TempPower.create(PowerStat.DEFENSE_REDUCTION, 20, 2, "Pela Technique Def Reduction");
            techniqueExposed.type = AbstractPower.PowerType.DEBUFF;
            enemy.addPower(techniqueExposed);
        }
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
        public void onAttack(Attack attack) {
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
