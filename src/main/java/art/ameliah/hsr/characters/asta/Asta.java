package art.ameliah.hsr.characters.asta;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
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

public class Asta extends AbstractCharacter<Asta> {

    public static final String NAME = "Asta";
    public static final String TALENT_BUFF_NAME = "Asta Talent Buff";
    private final AstaTalentPower talentPower;
    public static final int MAX_STACKS = 5;
    private boolean justCastUlt = false;

    public Asta() {
        super(NAME, 1023, 512, 463, 106, 80, ElementType.FIRE, 120, 100, Path.HARMONY);

        this.addPower(new TracePower()
                .setStat(PowerStat.FIRE_DMG_BOOST, 22.4f)
                .setStat(PowerStat.DEF_PERCENT, 22.5f)
                .setStat(PowerStat.CRIT_CHANCE, 6.7f));

        talentPower = new AstaTalentPower();
        skillEnergyGain = 36;

        this.registerGoal(0, new AlwaysUltGoal<>(this));
        this.registerGoal(0, new AlwaysSkillGoal<>(this));
    }

    @Override
    public void useSkill() {
        Attack attack = this.startAttack();

        attack.hitEnemy(getBattle().getEnemyWithHighestHP(), 0.55f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.SKILL);
        for (int numBounces = 0; numBounces < 5; numBounces++) {
            attack.hitEnemy(getBattle().getRandomEnemy(), 0.55F, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.SKILL);
        }

        attack.execute();
    }

    @Override
    public void useBasic() {
        this.startAttack()
                .hitEnemy(getBattle().getEnemyWithHighestHP(), 1.1f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.BASIC)
                .execute();
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
            character.addPower(PermPower.create(PowerStat.FIRE_DMG_BOOST, 18, "Asta Fire Damage Bonus"));
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

        this.startAttack()
                .hitEnemies(getBattle().getEnemies(), 0.5f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT)
                .execute();
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
        public void afterAttackFinish(Attack attack) {
            if (attack.getSource() == Asta.this) {
                int chargeGain = attack.getTargets().size();
                for (AbstractEnemy enemy :attack.getTargets()) {
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
