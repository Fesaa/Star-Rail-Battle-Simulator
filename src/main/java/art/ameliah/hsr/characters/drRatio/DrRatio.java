package art.ameliah.hsr.characters.drRatio;

import art.ameliah.hsr.battleLogic.combat.AttackLogic;
import art.ameliah.hsr.battleLogic.log.lines.character.DoMove;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysSkillGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.characters.goal.shared.ult.DontUltMissingPowerGoal;
import art.ameliah.hsr.characters.goal.shared.ult.UltAtEndOfBattle;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;

import java.util.List;

public class DrRatio extends AbstractCharacter<DrRatio> {
    public static final String NAME = "Dr. Ratio";

    public DrRatio() {
        super(NAME, 1048, 776, 461, 103, 80, ElementType.IMAGINARY, 140, 75, Path.HUNT);
        this.isDPS = true;
        this.hasAttackingUltimate = true;

        this.addPower(new TracePower()
                .setStat(PowerStat.ATK_PERCENT, 28)
                .setStat(PowerStat.CRIT_CHANCE, 12)
                .setStat(PowerStat.DEF_PERCENT, 12.5f));

        this.registerGoal(10, new AlwaysSkillGoal<>(this, 1));

        this.registerGoal(0, new UltAtEndOfBattle<>(this));
        this.registerGoal(20, DontUltMissingPowerGoal.robin(this));
        this.registerGoal(100, new AlwaysUltGoal<>(this));
        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));
    }

    public void useSkill() {
        this.doAttack(DamageType.SKILL, MoveType.SKILL, (e, al) -> {
            e.addPower(TempPower.createDebuff(PowerStat.EFFECT_RES, -10, 2, "RatioEffectResDebuff"));
            int debuffs = (int) e.powerList.stream().filter(p -> p.type == AbstractPower.PowerType.DEBUFF).count();
            for (int i = 0; i < debuffs; i++) {
                this.addPower(new Summation());
            }

            al.hit(e, 1.5f, TOUGHNESS_DAMAGE_TWO_UNITS);

            // Assume he always gets the FUA from his Skill
            this.useFollowUp(e);
        });
    }

    public void useBasic() {
        this.doAttack(DamageType.BASIC, MoveType.BASIC, (e, al) -> al.hit(e, 1, TOUGHNESS_DAMAGE_SINGLE_UNIT));
    }

    public void useFollowUp(final AbstractEnemy enemy) {
        this.actionMetric.record(MoveType.FOLLOW_UP);
        getBattle().addToLog(new DoMove(this, MoveType.FOLLOW_UP));
        increaseEnergy(5, FUA_ENERGY_GAIN);

        this.doAttack(DamageType.FOLLOW_UP, dh -> {
            AbstractEnemy target = enemy.isDead() ? getBattle().getRandomEnemy() : enemy;
            dh.logic(target, al -> al.hit(target, 2.7f, TOUGHNESS_DAMAGE_SINGLE_UNIT));
        });
    }

    public void useUltimate() {


        this.doAttack(DamageType.ULTIMATE, MoveType.ULTIMATE, (e, al) -> {
            al.hit(e, 2.4f, TOUGHNESS_DAMAGE_THREE_UNITs);
            getBattle().getEnemies().forEach(enemy -> enemy.removePower(WisemanFolly.NAME));
            e.addPower(new WisemanFolly());
        });
    }

    public void onCombatStart() {
        addPower(new Deduction());
    }

    private static class Deduction extends PermPower {
        public Deduction() {
            this.setName(this.getClass().getSimpleName());
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            int debuffs = Math.min(5, (int) enemy.powerList.stream().filter(p -> p.type == PowerType.DEBUFF).count());
            if (debuffs < 3) {
                return 0;
            }
            return debuffs * 10;
        }
    }

    private static class Summation extends PermPower {
        public Summation() {
            this.setName(this.getClass().getSimpleName());
            this.maxStacks = 6;
        }

        @Override
        public float getConditionalCritRate(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            return stacks * 2.5f;
        }

        @Override
        public float getConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            return stacks * 5;
        }
    }

    private class WisemanFolly extends PermPower {
        public static String NAME = "Wiseman Folly";
        private int numCharges = 2;

        public WisemanFolly() {
            this.name = name;
        }

        @Override
        public void afterAttacked(AttackLogic attack) {
            if (attack.getSource() != DrRatio.this) {
                if (numCharges > 0) {
                    numCharges--;
                    DrRatio.this.useFollowUp((AbstractEnemy) this.getOwner());
                    if (numCharges <= 0) {
                        this.getOwner().removePower(this);
                    }
                }
            }
        }
    }
}
