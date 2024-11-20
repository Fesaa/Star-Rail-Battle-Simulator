package enemies;

import battleLogic.AbstractEntity;
import battleLogic.BattleEvents;
import battleLogic.log.lines.enemy.ForcedAttack;
import battleLogic.log.lines.enemy.ReduceToughness;
import battleLogic.log.lines.enemy.RuanMeiDelay;
import battleLogic.log.lines.enemy.WeaknessBreakRecover;
import characters.AbstractCharacter;
import characters.ruanmei.RuanMei;
import powers.AbstractPower;
import powers.PowerStat;
import powers.TauntPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractEnemy extends AbstractEntity {
    public static final int DEFAULT_RES = 20;

    public final EnemyType type;

    protected final int baseHP;
    protected final int baseATK;
    protected final int baseDEF;
    protected final int toughness;
    protected final int level;

    private final Map<AbstractCharacter.ElementType, Integer> resMap = new HashMap<>();
    private final List<AbstractCharacter.ElementType> weaknessMap = new ArrayList<>();

    // Moc increases hp this way
    protected int HPMultiplier;

    public int numAttacksMetric = 0;
    public int numSingleTargetMetric = 0;
    public int numBlastMetric = 0;
    public int numAoEMetric = 0;
    public int timesBrokenMetric = 0;

    protected float currentHp = 0;
    protected float currentToughness = 0;


    public AbstractEnemy(String name, EnemyType type, int baseHP, int baseATK, int baseDEF, int baseSpeed, int toughness, int level) {
        this.name = name;
        this.type = type;
        this.baseHP = baseHP;
        this.baseATK = baseATK;
        this.baseDEF = baseDEF;
        this.baseSpeed = baseSpeed;
        this.toughness = toughness;
        this.level = level;
        this.setUpDefaultRes();
    }

    public int getLevel() {
        return level;
    }

    public boolean isWeaknessBroken() {
        return this.currentToughness == 0;
    }

    public float maxToughness() {
        return this.toughness;
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public void setHPMultiplier(int HPMultiplier) {
        this.HPMultiplier = HPMultiplier;
    }

    public float getFinalAttack() {
        float totalBonusAtkPercent = 0;
        float totalBonusFlatAtk = 0;
        for (AbstractPower power : powerList) {
            totalBonusAtkPercent += power.getStat(PowerStat.ATK_PERCENT);
            totalBonusFlatAtk += power.getStat(PowerStat.FLAT_ATK);
        }
        return (int) ((float) this.baseATK * (1 + totalBonusAtkPercent / 100) + totalBonusFlatAtk);
    }

    public float getFinalDefense() {
        float totalDefenseBonus = 0;
        float totalDefenseReduction = 0;
        for (AbstractPower power : powerList) {
            totalDefenseBonus += power.getStat(PowerStat.DEF_PERCENT);
            totalDefenseReduction += power.getStat(PowerStat.DEFENSE_REDUCTION);
        }
        return totalDefenseBonus - totalDefenseReduction;
    }

    public void setUpDefaultRes() {
        resMap.put(AbstractCharacter.ElementType.FIRE, DEFAULT_RES);
        resMap.put(AbstractCharacter.ElementType.ICE, DEFAULT_RES);
        resMap.put(AbstractCharacter.ElementType.WIND, DEFAULT_RES);
        resMap.put(AbstractCharacter.ElementType.LIGHTNING, DEFAULT_RES);
        resMap.put(AbstractCharacter.ElementType.PHYSICAL, DEFAULT_RES);
        resMap.put(AbstractCharacter.ElementType.QUANTUM, DEFAULT_RES);
        resMap.put(AbstractCharacter.ElementType.IMAGINARY, DEFAULT_RES);
    }

    public void setRes(AbstractCharacter.ElementType elementType, int resValue) {
        resMap.put(elementType, resValue);
    }

    public int getRes(AbstractCharacter.ElementType elementType) {
        return resMap.get(elementType);
    }

    public void addWeakness(AbstractCharacter.ElementType elementType) {
        this.weaknessMap.add(elementType);
    }

    public void removeWeakness(AbstractCharacter.ElementType elementType) {
        this.weaknessMap.remove(elementType);
    }

    public boolean hasWeakness(AbstractCharacter.ElementType elementType) {
        return this.weaknessMap.contains(elementType);
    }

    protected int getRandomTargetPosition() {
        AbstractPower taunt = getPower(TauntPower.class.getSimpleName());

        if (taunt instanceof TauntPower) {
            AbstractCharacter<?> taunter = ((TauntPower) taunt).taunter;
            getBattle().addToLog(new ForcedAttack(this, taunter));
            for (int i = 0; i < getBattle().getPlayers().size(); i++) {
                if (getBattle().getPlayers().get(i) == taunter) {
                    return i;
                }
            }

            throw new RuntimeException("Taunter not present in battle.");
        }


        // This code included a correcting from the previous code.
        // It would use the position of the target inside validTargets
        // This would be the wrong position to use later on.
        // We use the new AbstractCharacter#canBeAttacked method to make sure a character can be attacked
        // Currently only Moze.

        double totalWeight = getBattle().getPlayers().stream().mapToDouble(AbstractCharacter::getFinalTauntValue).sum();
        int characterPosition = 0;
        // TODO: Check if it actually checks like this. I'd think that this way the first position gets attacked lot more
        // even with same taunt. As it just gets checked first. Not sure.

        double r = getBattle().getEnemyTargetRng().nextDouble() * totalWeight;
        for (int pos = 0; pos < getBattle().getPlayers().size(); pos++) {
            AbstractCharacter<?> character = getBattle().getPlayers().get(pos);
            if (!character.canBeAttacked()) {
                // Do not update r value if character cannot be attacked. See todo for concerns.
                continue;
            }

            r -= getBattle().getPlayers().get(pos).getFinalTauntValue();
            if (r <= 0) {
                return pos;
            }
        }

        // Attack last character is no character is found
        return getBattle().getPlayers().size() -1;
    }

    // TODO: Implement this correctly with buffs etc.
    protected float attackDmg() {
        return this.getFinalAttack();
    }

    public void reduceToughness(float amount) {
        if (this.currentToughness == 0) {
            return;
        }
        float initialToughness = this.currentToughness;
        this.currentToughness = Math.max(initialToughness - amount, 0);

        getBattle().addToLog(new ReduceToughness(this, amount, initialToughness, this.currentToughness));

        if (this.currentToughness == 0) {
            this.timesBrokenMetric++;
            getBattle().DelayEntity(this, 25);
            getBattle().getPlayers().forEach(p -> {
                p.onWeaknessBreak(this);
            });
            this.emit(BattleEvents::onWeaknessBreak);
        }
    }

    @Override
    public void onCombatStart() {
        this.currentHp = this.baseHP * this.HPMultiplier;
        this.currentToughness = this.toughness;
    }

    /**
     * Weakness bar logic
     */
    @Override
    public void onTurnStart() {
        if (!this.isWeaknessBroken()) {
            return;
        }

        RuanMei.RuanMeiUltDebuff ruanMeiDebuff = (RuanMei.RuanMeiUltDebuff) this.getPower(RuanMei.ULT_DEBUFF_NAME);
        if (ruanMeiDebuff != null && !ruanMeiDebuff.triggered) {
            getBattle().addToLog(new RuanMeiDelay());
            float delay = ruanMeiDebuff.owner.getTotalBreakEffect() * 0.2f + 10;
            getBattle().DelayEntity(this, delay);
            ruanMeiDebuff.triggered = true;
            //getBattle().getHelper().breakDamageHitEnemy(ruanMeiDebuff.owner, this, 0.5f);
            return;
        }

        this.removePower(RuanMei.ULT_DEBUFF_NAME);
        getBattle().addToLog(new WeaknessBreakRecover(this, this.currentHp, this.toughness));
        this.currentToughness = this.toughness;
    }

    @Override
    public void takeTurn() {
        if (this.isWeaknessBroken()) {
            return;
        }

        this.act();
        numTurnsMetric++;
    }

    /**
     * The next action returns true, to continue acting (i.e. double action)
     */
    abstract protected void act();
    abstract public String getMetrics();
}
