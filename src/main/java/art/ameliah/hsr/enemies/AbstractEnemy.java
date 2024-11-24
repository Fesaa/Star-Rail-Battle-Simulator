package art.ameliah.hsr.enemies;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.BattleEvents;
import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.combat.EnemyAttack;
import art.ameliah.hsr.battleLogic.combat.hit.Hit;
import art.ameliah.hsr.battleLogic.combat.result.HitResult;
import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyDied;
import art.ameliah.hsr.battleLogic.log.lines.enemy.ForcedAttack;
import art.ameliah.hsr.battleLogic.log.lines.enemy.ReduceToughness;
import art.ameliah.hsr.battleLogic.log.lines.enemy.RuanMeiDelay;
import art.ameliah.hsr.battleLogic.log.lines.enemy.WeaknessBreakRecover;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.ruanmei.RuanMei;
import art.ameliah.hsr.enemies.action.EnemyActionSequence;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TauntPower;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractEnemy extends AbstractEntity {
    public static final int DEFAULT_RES = 20;

    public final EnemyType type;

    protected final int baseHP;
    protected final int baseATK;
    protected final int baseDEF;
    protected final int toughness;
    @Getter
    protected final int level;

    protected final Map<ElementType, Integer> resMap = new HashMap<>();
    protected final Set<ElementType> weaknessMap = new HashSet<>();
    protected final EnemyActionSequence sequence;
    public int numAttacksMetric = 0;
    public int numSingleTargetMetric = 0;
    public int numBlastMetric = 0;
    public int numAoEMetric = 0;
    public int timesBrokenMetric = 0;
    // Moc increases hp this way
    @Setter
    protected int HPMultiplier = 1;
    @Getter
    protected float currentHp = 0;
    protected float currentToughness = 0;

    public AbstractEnemy(String name, EnemyType type, int baseHP, int baseATK, int baseDEF, float baseSpeed, int toughness, int level) {
        this.name = name;
        this.type = type;
        this.baseHP = baseHP;
        this.baseATK = baseATK;
        this.baseDEF = baseDEF;
        this.baseSpeed = baseSpeed;
        this.toughness = toughness;
        this.level = level;
        this.setUpDefaultRes();

        this.sequence = new EnemyActionSequence(this);
    }

    public boolean isWeaknessBroken() {
        return this.currentToughness <= 0;
    }

    public float maxToughness() {
        return this.toughness;
    }

    public float maxHp() {
        return this.baseHP;
    }

    public float getBaseSpeed() {
        return baseSpeed;
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
        resMap.put(ElementType.FIRE, DEFAULT_RES);
        resMap.put(ElementType.ICE, DEFAULT_RES);
        resMap.put(ElementType.WIND, DEFAULT_RES);
        resMap.put(ElementType.LIGHTNING, DEFAULT_RES);
        resMap.put(ElementType.PHYSICAL, DEFAULT_RES);
        resMap.put(ElementType.QUANTUM, DEFAULT_RES);
        resMap.put(ElementType.IMAGINARY, DEFAULT_RES);
    }

    public void setRes(ElementType elementType, int resValue) {
        resMap.put(elementType, resValue);
    }

    public int getRes(ElementType elementType) {
        return resMap.get(elementType);
    }

    public boolean addWeakness(ElementType elementType) {
        this.setRes(elementType, 0);
        return this.weaknessMap.add(elementType);
    }

    public void removeWeakness(ElementType elementType) {
        this.weaknessMap.remove(elementType);
        this.setRes(elementType, DEFAULT_RES);
    }

    public boolean hasWeakness(ElementType elementType) {
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
        return getBattle().getPlayers().size() - 1;
    }

    protected AbstractCharacter<?> getRandomTarget() {
        return getBattle().getPlayers().get(getRandomTargetPosition());
    }

    protected List<AbstractCharacter<?>> getRandomTargets(int amount) {
        List<AbstractCharacter<?>> targets = new ArrayList<>();
        while (targets.size() < amount) {
            AbstractCharacter<?> target = getRandomTarget();
            if (targets.contains(target)) {
                continue;
            }
            targets.add(target);
        }

        return targets;
    }

    protected boolean successfulHit(AbstractCharacter<?> target, double chance) {
        double extraEHR = this.powerList.stream()
                .mapToDouble(p -> p.getStat(PowerStat.EFFECT_HIT))
                .sum();

        double effectRes = target.getTotalEffectRes();
        double realChance = chance / 100 * (1 + extraEHR / 100) * (1 - effectRes / 100);

        return getBattle().getEnemyEHRRng().nextDouble() < realChance;
    }

    // TODO: Implement this correctly with buffs etc.
    public float attackDmg() {
        return this.getFinalAttack();
    }

    public HitResult hit(Hit hit) {
        if (this.isDead()) {
            return new HitResult(hit, 0, 0, false, false);
        }

        final float dmgToDeal = Math.min(hit.finalDmg(), this.currentHp);

        this.currentHp -= dmgToDeal;

        if (this.isWeaknessBroken()) {
            return new HitResult(hit, dmgToDeal, 0, false, this.isDead());
        }

        float initialToughness = this.currentToughness;
        float toughnessToDeal = Math.min(hit.finalDmg(), this.currentToughness);
        this.currentToughness -= toughnessToDeal;
        getBattle().addToLog(new ReduceToughness(this, hit.finalToughnessReduction(), initialToughness, this.currentToughness));

        if (this.currentToughness == 0) {
            this.timesBrokenMetric++;
            getBattle().DelayEntity(this, 25);
            getBattle().getPlayers().forEach(p -> p.onWeaknessBreak(this));
            this.emit(BattleEvents::onWeaknessBreak);
        }

        return new HitResult(hit, dmgToDeal, toughnessToDeal, this.isWeaknessBroken(), this.isDead());
    }

    public boolean isDead() {
        return this.currentHp <= 0;
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
        getBattle().addToLog(new WeaknessBreakRecover(this, this.currentToughness, this.toughness));
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

    @Override
    public void afterAttacked(Attack attack) {
        if (this.currentHp > 0) {
            return;
        }

        getBattle().addToLog(new EnemyDied(this, attack.getSource()));
        getBattle().removeEnemy(this);
        this.emit(BattleEvents::onDeath);
    }

    public String getMetrics() {
        return String.format("Metrics for %s with %,f speed \nTurns taken: %,d \nTotal attacks: %,d \nSingle-target attacks: %,d \nBlast attacks: %,d \nAoE attacks: %,d \nWeakness Broken: %,d", name, baseSpeed, numTurnsMetric, numAttacksMetric, numSingleTargetMetric, numBlastMetric, numAoEMetric, timesBrokenMetric);
    }

    public EnemyAttack startAttack() {
        return new EnemyAttack(this);
    }

    /**
     * The next action returns true, to continue acting (i.e. double action)
     */
    abstract protected void act();
}
