package art.ameliah.hsr.characters.asta;

import art.ameliah.hsr.battleLogic.BattleHelpers;
import art.ameliah.hsr.battleLogic.log.lines.entity.GainCharge;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.AlwaysSkillGoal;
import art.ameliah.hsr.characters.goal.shared.AlwaysUltGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;

import java.util.ArrayList;
import java.util.Set;

public class Asta extends AbstractCharacter<Asta> {

    public static final String NAME = "Asta";
    public static final String TALENT_BUFF_NAME = "Asta Talent Buff";
    private final AstaTalentPower talentPower;
    public static final int MAX_STACKS = 5;
    private boolean justCastUlt = false;

    public Asta() {
        super(NAME, 1023, 512, 463, 106, 80, ElementType.FIRE, 120, 100, Path.HARMONY);

        this.addPower(new TracePower()
                .setStat(PowerStat.SAME_ELEMENT_DAMAGE_BONUS, 22.4f)
                .setStat(PowerStat.DEF_PERCENT, 22.5f)
                .setStat(PowerStat.CRIT_CHANCE, 6.7f));

        talentPower = new AstaTalentPower();
        skillEnergyGain = 36;

        this.registerGoal(0, new AlwaysUltGoal<>(this));
        this.registerGoal(0, new AlwaysSkillGoal<>(this));
    }

    public void useSkill() {
        ArrayList<DamageType> types = new ArrayList<>();
        types.add(DamageType.SKILL);
        getBattle().getHelper().PreAttackLogic(this, types);

        AbstractEnemy enemy = getBattle().getMiddleEnemy();
        getBattle().getHelper().hitEnemy(this, enemy, 0.55f, BattleHelpers.MultiplierStat.ATK, types, TOUGHNESS_DAMAGE_SINGLE_UNIT);

        int numBounces = 5;
        while (numBounces > 0) {
            getBattle().getHelper().hitEnemy(this, getBattle().getRandomEnemy(), 0.55f, BattleHelpers.MultiplierStat.ATK, types, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            numBounces--;
        }

        getBattle().getHelper().PostAttackLogic(this, types);
    }
    public void useBasic() {
        ArrayList<DamageType> types = new ArrayList<>();
        types.add(DamageType.BASIC);
        getBattle().getHelper().PreAttackLogic(this, types);

        AbstractEnemy enemy = getBattle().getMiddleEnemy();
        getBattle().getHelper().hitEnemy(this, enemy, 1.1f, BattleHelpers.MultiplierStat.ATK, types, TOUGHNESS_DAMAGE_SINGLE_UNIT);

        getBattle().getHelper().PostAttackLogic(this, types);
    }

    public void useUltimate() {
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            getBattle().IncreaseSpeed(character, TempPower.create(PowerStat.FLAT_SPEED, 53, 2, "Asta Ult Speed Buff"));
        }
        justCastUlt = true;
    }

    public void onCombatStart() {
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.addPower(talentPower);
            if (character.elementType == ElementType.FIRE) {
                character.addPower(PermPower.create(PowerStat.SAME_ELEMENT_DAMAGE_BONUS, 18, "Asta Fire Damage Bonus"));
            }
        }
        addPower(new AstaERRPower());
    }

    public void onTurnStart() {
        if (justCastUlt) {
            justCastUlt = false;
            return;
        }
        // check for turn 1 since this metric is incremented in takeTurn, which happens after onTurnStart. So at the time of the 2nd onTurnStart, this metric will still be 1.
        if (numTurnsMetric >= 1) {
            decreaseStacks(2);
        }
    }

    public void useTechnique() {
        if (getBattle().usedEntryTechnique()) {
            return;
        } else {
            getBattle().setUsedEntryTechnique(true);
        }
        ArrayList<DamageType> types = new ArrayList<>();
        getBattle().getHelper().PreAttackLogic(this, types);

        for (AbstractEnemy enemy : getBattle().getEnemies()) {
            getBattle().getHelper().hitEnemy(this, enemy, 0.5f, BattleHelpers.MultiplierStat.ATK, types, TOUGHNESS_DAMAGE_SINGLE_UNIT);
        }

        getBattle().getHelper().PostAttackLogic(this, types);
    }

    public void increaseStacks(int amount) {
        int initalStack = talentPower.stacks;
        talentPower.stacks += amount;
        if (talentPower.stacks > MAX_STACKS) {
            talentPower.stacks = MAX_STACKS;
        }
        getBattle().addToLog(new GainCharge(this, amount, initalStack, talentPower.stacks, "Stack"));
    }

    public void decreaseStacks(int amount) {
        int initalStack = talentPower.stacks;
        talentPower.stacks -= amount;
        if (talentPower.stacks < 0) {
            talentPower.stacks = 0;
        }
        getBattle().addToLog(new GainCharge(this, amount, initalStack, talentPower.stacks, "Stack"));
    }

    private class AstaTalentPower extends AbstractPower {

        public AstaTalentPower() {
            this.name = TALENT_BUFF_NAME;
            this.lastsForever = true;
            this.stacks = 0;
        }

        @Override
        public void onAttack(AbstractCharacter<?> character, Set<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
            if (character == Asta.this) {
                int chargeGain = enemiesHit.size();
                for (AbstractEnemy enemy : enemiesHit) {
                    if (enemy.hasWeakness(ElementType.FIRE)) {
                        chargeGain++;
                    }
                }
               increaseStacks(chargeGain);
            }
        }

        @Override
        public float getConditionalAtkBonus(AbstractCharacter<?> character) {
            return 15.4f * stacks;
        }
    }

    private class AstaERRPower extends AbstractPower {

        public AstaERRPower() {
            this.name = this.getClass().getSimpleName();
            this.lastsForever = true;
        }

        @Override
        public float getConditionalERR(AbstractCharacter<?> character) {
            if (talentPower.stacks >= 2) {
                return 15;
            }
            return 0;
        }
    }
}
