package art.ameliah.hsr.characters.drRatio;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.battleLogic.log.lines.character.DoMove;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrRatio extends AbstractCharacter<DrRatio> {
    public static final String NAME = "Dr. Ratio";
    private final String numFUAsMetricName = "Follow up Attacks used";
    private int numFUAs = 0;

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
    }

    public void useSkill() {
        AbstractEnemy enemy = getBattle().getEnemyWithHighestHP();

        enemy.addPower(TempPower.createDebuff(PowerStat.EFFECT_RES, -10, 2, "RatioEffectResDebuff"));
        int debuffs = (int) enemy.powerList.stream().filter(p -> p.type == AbstractPower.PowerType.DEBUFF).count();
        for (int i = 0; i < debuffs; i++) {
            this.addPower(new Summation());
        }

        this.startAttack()
                .hitEnemy(enemy, 1.5f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_TWO_UNITS, DamageType.SKILL)
                .execute();

        // Assume he always gets the FUA from his Skill
        useFollowUp(enemy);
    }

    public void useBasic() {
        this.startAttack()
                .hitEnemy(getBattle().getEnemyWithHighestHP(), 1, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.BASIC)
                .execute();
    }

    public void useFollowUp(AbstractEnemy enemy) {
        if (enemy.isDead()) {
            enemy = getBattle().getEnemyWithHighestHP();
        }

        moveHistory.add(MoveType.FOLLOW_UP);
        numFUAs++;
        getBattle().addToLog(new DoMove(this, MoveType.FOLLOW_UP));
        increaseEnergy(5, FUA_ENERGY_GAIN);

        this.startAttack()
                .hitEnemy(enemy, 2.7f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.FOLLOW_UP)
                .execute();
    }

    public void useUltimate() {
        AbstractEnemy enemy = getBattle().getEnemyWithHighestHP();

        this.startAttack()
                .hitEnemy(enemy, 2.4f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_THREE_UNITs, DamageType.ULTIMATE)
                .execute();

        enemy.addPower(new WisemanFolly());
    }

    public void onCombatStart() {
        addPower(new Deduction());
    }

    public HashMap<String, String> getCharacterSpecificMetricMap() {
        HashMap<String, String> map = super.getCharacterSpecificMetricMap();
        map.put(numFUAsMetricName, String.valueOf(numFUAs));
        return map;
    }

    public ArrayList<String> getOrderedCharacterSpecificMetricsKeys() {
        ArrayList<String> list = super.getOrderedCharacterSpecificMetricsKeys();
        list.add(numFUAsMetricName);
        return list;
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
        private int numCharges = 2;

        public WisemanFolly() {
            this.setName(this.getClass().getSimpleName());
        }

        @Override
        public void afterAttacked(Attack attack) {
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
