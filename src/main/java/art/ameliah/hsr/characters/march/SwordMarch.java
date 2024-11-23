package art.ameliah.hsr.characters.march;

import art.ameliah.hsr.battleLogic.BattleEvents;
import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.combat.Hit;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.battleLogic.log.lines.character.DoMove;
import art.ameliah.hsr.battleLogic.log.lines.character.ExtraHits;
import art.ameliah.hsr.battleLogic.log.lines.entity.GainCharge;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.AlwaysBasicGoal;
import art.ameliah.hsr.characters.goal.shared.AlwaysUltGoal;
import art.ameliah.hsr.characters.goal.shared.SkillFirstTurnGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SwordMarch extends AbstractCharacter<SwordMarch> implements SkillFirstTurnGoal.FirstTurnTracked {
    public static final String NAME = "Sword March";

    private Random fuaRng;
    public AbstractCharacter<?> master;

    private int numEBA = 0;
    private int numFUAs = 0;
    private int numUltEnhancedEBA;
    private int totalNumExtraHits;
    private final String numEBAMetricName = "Enhanced Basic Attacks used";
    private final String numFUAsMetricName = "Follow up Attacks used";
    private final String numUltEnhancedEBAUsed = "Ult Boosted Enhanced Basic Attacks used";
    private final String numExtraHitsMetricName = "Number of extra hits with EBA";
    private final String leftoverChargeMetricName = "Leftover Charge";
    public int chargeCount = 0;
    public final int chargeThreshold = 7;
    private boolean isEnhanced;
    private boolean hasUltEnhancement;
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
    }

    // Assuming always dps
    private void masterEffect(Attack attack, AbstractEnemy target) {
        if (master != null) {
            boolean ignore = target.hasWeakness(this.elementType) || target.hasWeakness(master.elementType);
            attack.hitEnemy(new Hit(this, target, 0.22f, MultiplierStat.ATK, List.of(), 0, this.master.elementType, true));
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
        Attack attack = this.startAttack();
        AbstractEnemy target = getBattle().getEnemyWithHighestHP();

        attack.hitEnemy(target, 1.1f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.BASIC);
        this.masterEffect(attack, target);

        gainCharge(1);

        attack.execute();
    }

    @Override
    protected void basicSequence() {
        if (!this.isEnhanced) {
            super.basicSequence();
            return;
        }

        this.emit(BattleEvents::onUseBasic);
        this.useEnhancedBasicAttack();
        this.emit(BattleEvents::afterUseBasic);
    }

    public void useEnhancedBasicAttack() {
        moveHistory.add(MoveType.ENHANCED_BASIC);
        numEBA++;
        getBattle().addToLog(new DoMove(this, MoveType.ENHANCED_BASIC));
        increaseEnergy(30, EBA_ENERGY_GAIN);

        Attack attack = this.startAttack();

        AbstractEnemy enemy = getBattle().getEnemyWithHighestHP();
        int initialHits = 3;
        int numExtraHits = 0;
        int procChance = 60;

        TempPower ultCritDmgBuff = new TempPower("March Ult Crit Dmg Buff");
        ultCritDmgBuff.setStat(PowerStat.CRIT_DAMAGE, 50);

        TempPower ebaDamageBonus = new TempPower("March Enhanced Basic Damage Bonus");
        ebaDamageBonus.setStat(PowerStat.DAMAGE_BONUS, 88);

        addPower(ebaDamageBonus);
        if (hasUltEnhancement) {
            initialHits += 2;
            procChance = 80;
            numUltEnhancedEBA++;
            addPower(ultCritDmgBuff);
        }
        for (int i = 0; i < 3; i++) {
            double roll = getBattle().getProcChanceRng().nextDouble() * 100 + 1;
            if (roll < procChance) {
                numExtraHits++;
            } else {
                break;
            }
        }
        totalNumExtraHits += numExtraHits;
        getBattle().addToLog(new ExtraHits(this, numExtraHits));
        for (int i = 0; i < initialHits + numExtraHits; i++) {
            attack.hitEnemy(enemy, 0.88f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_HALF_UNIT, DamageType.BASIC);
            this.masterEffect(attack, enemy);
        }
        attack.addAfterAttack(() -> {
            master.addPower(TempPower.create(PowerStat.CRIT_DAMAGE, 60, 2,"Enhanced Basic Master Buff"));
        });
        attack.execute();

        if (hasUltEnhancement) {
            hasUltEnhancement = false;
            removePower(ultCritDmgBuff);
        }
        removePower(ebaDamageBonus);
        isEnhanced = false;
    }

    public void useFollowUp(AbstractEnemy enemy) {
        if (FUAReady) {
            FUAReady = false;
            moveHistory.add(MoveType.FOLLOW_UP);
            numFUAs++;
            getBattle().addToLog(new DoMove(this, MoveType.FOLLOW_UP));

            Attack attack = this.startAttack();
            attack.hitEnemy(enemy, 0.6f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.FOLLOW_UP);
            this.masterEffect(attack, enemy);

            attack.addAfterAttack(() -> {
                increaseEnergy(5, FUA_ENERGY_GAIN);
                gainCharge(1);
            });

            attack.execute();
        }
    }

    public void useUltimate() {
        this.hasUltEnhancement = true;
        this.startAttack()
                .hitEnemy(getBattle().getEnemyWithHighestHP(), 2.59f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_THREE_UNITs, DamageType.ULTIMATE)
                .execute();
    }

    public void onTurnStart() {
        FUAReady = true;
        increaseEnergy(5, "from E4");
        if (currentEnergy >= ultCost) {
            tryUltimate();
        }
    }

    @Override
    public void onCombatStart() {
        this.fuaRng = new Random(getBattle().getSeed());
        getBattle().AdvanceEntity(this, 25);
    }

    @Override
    public void useTechnique() {
        gainCharge(3);
        increaseEnergy(30, TECHNIQUE_ENERGY_GAIN);
    }

    public void gainCharge(int amount) {
        int initialCharge = this.chargeCount;
        this.chargeCount += amount;
        getBattle().addToLog(new GainCharge(this, amount, initialCharge, this.chargeCount));
        if (this.chargeCount >= chargeThreshold) {
            this.chargeCount -= chargeThreshold;
            getBattle().AdvanceEntity(this, 100);
            isEnhanced = true;
        }
    }

    public HashMap<String, String> getCharacterSpecificMetricMap() {
        HashMap<String, String> map = super.getCharacterSpecificMetricMap();
        map.put(leftoverChargeMetricName, String.valueOf(chargeCount));
        map.put(numFUAsMetricName, String.valueOf(numFUAs));
        map.put(numEBAMetricName, String.valueOf(numEBA));
        map.put(numUltEnhancedEBAUsed, String.valueOf(numUltEnhancedEBA));
        map.put(numExtraHitsMetricName, String.valueOf(totalNumExtraHits));
        return map;
    }

    public ArrayList<String> getOrderedCharacterSpecificMetricsKeys() {
        ArrayList<String> list = super.getOrderedCharacterSpecificMetricsKeys();
        list.add(leftoverChargeMetricName);
        list.add(numFUAsMetricName);
        list.add(numEBAMetricName);
        list.add(numUltEnhancedEBAUsed);
        list.add(numExtraHitsMetricName);
        return list;
    }

    @Override
    public boolean isFirstTurn() {
        return firstMove;
    }

    @Override
    public void setFirstTurn(boolean firstTurn) {
        firstMove = firstTurn;
    }

    private class MarchMasterPower extends PermPower {
        public MarchMasterPower() {
            this.name = this.getClass().getSimpleName();
            this.setStat(PowerStat.SPEED_PERCENT, 10.8f);
        }

        @Override
        public void onAttack(Attack attack) {
            SwordMarch.this.gainCharge(1);
        }

        @Override
        public void afterAttackFinish(Attack attack) {
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

        @Override
        public void onUseUltimate() {
            if (!master.hasAttackingUltimate) {
                SwordMarch.this.gainCharge(1);
            }
        }
    }
}
