package art.ameliah.hsr.characters.aventurine;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.battleLogic.log.lines.character.aventurine.UseBlindBet;
import art.ameliah.hsr.battleLogic.log.lines.entity.GainCharge;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.AlwaysUltGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TracePower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Aventurine extends AbstractCharacter<Aventurine> {
    public static final String NAME = "Aventurine";

    final AventurineTalentPower talentPower = new AventurineTalentPower();
    private int numFollowUps = 0;
    private int numBlindBetGained = 0;
    private int numBlindBetGainedFUA = 0;
    private int blindBetCounter = 0;
    private final int BLIND_BET_THRESHOLD = 7;
    private static final int BLIND_BET_CAP = 10;
    private final int blindBetFollowUpPerTurn = 3;
    private int blindBetFollowUpCounter = blindBetFollowUpPerTurn;
    final boolean SPNeutral;
    private final String numFollowUpsMetricName = "Follow up Attacks used";
    private final String numBlindBetGainedMetricName = "Blind Bet gained";
    private final String numBlindBetFromFUAMetricName = "Blind Bet gained from Ally FUA";

    public Aventurine(boolean SPNeutral) {
        super(NAME, 1203, 446, 655, 106, 80, ElementType.IMAGINARY, 110, 150, Path.PRESERVATION);

        this.SPNeutral = SPNeutral;
        this.addPower(new TracePower()
                .setStat(PowerStat.DEF_PERCENT, 35)
                .setStat(PowerStat.IMAGINARY_DMG_BOOST, 14.4f)
                .setStat(PowerStat.EFFECT_RES, 10));
        this.hasAttackingUltimate = true;

        this.registerGoal(0, new AlwaysUltGoal<>(this));
        this.registerGoal(0, new AventurineTurnGoal(this));
    }

    public Aventurine() {
        this(true);
    }

    @Override
    protected void useSkill() {
        // Shields are not implemented
    }

    public void useBasic() {
        this.startAttack()
                .hitEnemy(getBattle().getEnemyWithHighestHP(), 1, MultiplierStat.DEF, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.BASIC)
                .execute();
    }

    public void useUltimate() {
        AbstractEnemy target = getBattle().getEnemyWithHighestHP();
        target.addPower(new AventurineUltDebuff());

        this.startAttack()
                .hitEnemy(target, 2.7f, MultiplierStat.DEF, TOUGHNESS_DAMAGE_THREE_UNITs, DamageType.ULTIMATE)
                .execute();

        int blindBetGain = getBattle().getGambleChanceRng().nextInt(7) + 1;
        increaseBlindBet(blindBetGain);
    }

    public void useFollowUp() {
        numFollowUps++;
        int initialBlindBet = this.blindBetCounter;
        this.blindBetCounter -= BLIND_BET_THRESHOLD;
        getBattle().addToLog(new UseBlindBet(this, initialBlindBet, this.blindBetCounter));
        increaseEnergy(7, FUA_ENERGY_GAIN);

        Attack attack = this.startAttack();
        for (int numBounces = 0; numBounces < 7; numBounces++) {
            attack.hitEnemy(getBattle().getRandomEnemy(), 0.25f, MultiplierStat.DEF, 3.3333333333333335f, DamageType.FOLLOW_UP);
        }
        attack.execute();
    }

    public void onCombatStart() {
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.addPower(talentPower);
        }
        addPower(PermPower.create(PowerStat.CRIT_CHANCE, 48, "Aventurine Crit Chance Bonus"));
    }

    public void onTurnStart() {
        blindBetFollowUpCounter = blindBetFollowUpPerTurn;
    }

    public void increaseBlindBet(int amount) {
        numBlindBetGained += amount;
        int initialBlindBet = this.blindBetCounter;
        this.blindBetCounter += amount;
        if (this.blindBetCounter > BLIND_BET_CAP) {
            this.blindBetCounter = BLIND_BET_CAP;
        }
        getBattle().addToLog(new GainCharge(this, amount, initialBlindBet, this.blindBetCounter, "Blind Bet"));
        if (this.blindBetCounter >= BLIND_BET_THRESHOLD) {
            useFollowUp();
        }
    }

    public HashMap<String, String> getCharacterSpecificMetricMap() {
        HashMap<String, String> map = super.getCharacterSpecificMetricMap();
        map.put(numFollowUpsMetricName, String.valueOf(numFollowUps));
        map.put(numBlindBetGainedMetricName, String.valueOf(numBlindBetGained));
        map.put(numBlindBetFromFUAMetricName, String.valueOf(numBlindBetGainedFUA));
        return map;
    }

    public ArrayList<String> getOrderedCharacterSpecificMetricsKeys() {
        ArrayList<String> list = super.getOrderedCharacterSpecificMetricsKeys();
        list.add(numFollowUpsMetricName);
        list.add(numBlindBetGainedMetricName);
        list.add(numBlindBetFromFUAMetricName);
        return list;
    }

    private static class AventurineUltDebuff extends AbstractPower {
        public AventurineUltDebuff() {
            this.name = this.getClass().getSimpleName();
            this.turnDuration = 3;
            this.type = PowerType.DEBUFF;
        }

        @Override
        public float receiveConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            return 15;
        }
    }

    private class AventurineTalentPower extends AbstractPower {
        public AventurineTalentPower() {
            this.name = this.getClass().getSimpleName();
            this.lastsForever = true;
            this.setStat(PowerStat.EFFECT_RES, 50); //assume 100% shield uptime
        }

        @Override
        public void onAttacked(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> types, int energyFromAttacked, float totalDmg) {
            if (character == Aventurine.this) {
                increaseBlindBet(2);
            } else {
                increaseBlindBet(1);
            }
        }

        @Override
        public void afterAttackFinish(Attack attack) {
            if (attack.getSource() != Aventurine.this && attack.getTypes().contains(DamageType.FOLLOW_UP) && blindBetFollowUpCounter > 0) {
                increaseBlindBet(1);
                blindBetFollowUpCounter--;
                numBlindBetGainedFUA++;
            }
        }
    }
}
