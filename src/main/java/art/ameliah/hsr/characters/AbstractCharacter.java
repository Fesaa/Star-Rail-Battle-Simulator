package art.ameliah.hsr.characters;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.BattleEvents;
import art.ameliah.hsr.battleLogic.combat.ally.Attack;
import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.battleLogic.combat.ally.DelayAttack;
import art.ameliah.hsr.battleLogic.combat.hit.EnemyHit;
import art.ameliah.hsr.battleLogic.combat.result.EnemyHitResult;
import art.ameliah.hsr.battleLogic.log.lines.character.DoMove;
import art.ameliah.hsr.battleLogic.log.lines.character.GainEnergy;
import art.ameliah.hsr.battleLogic.log.lines.character.TurnDecision;
import art.ameliah.hsr.battleLogic.log.lines.character.UltDecision;
import art.ameliah.hsr.characters.goal.AllyTargetGoal;
import art.ameliah.hsr.characters.goal.EnemyTargetGoal;
import art.ameliah.hsr.characters.goal.TurnGoal;
import art.ameliah.hsr.characters.goal.UltGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.lightcones.DefaultLightcone;
import art.ameliah.hsr.metrics.ActionMetric;
import art.ameliah.hsr.metrics.CounterMetric;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class AbstractCharacter<C extends AbstractCharacter<C>> extends AbstractEntity {

    protected static final String ENERGY_KEY = "energy";
    protected static final float TOUGHNESS_DAMAGE_HALF_UNIT = 5;
    protected static final float TOUGHNESS_DAMAGE_SINGLE_UNIT = 10;
    protected static final float TOUGHNESS_DAMAGE_TWO_UNITS = 20;
    protected static final float TOUGHNESS_DAMAGE_THREE_UNITs = 30;

    public static final String ULT_ENERGY_GAIN = "from using Ultimate";
    public static final String SKILL_ENERGY_GAIN = "from using Skill";
    public static final String BASIC_ENERGY_GAIN = "from using Basic";
    public static final String EBA_ENERGY_GAIN = "from using Enhanced Basic";
    public static final String FUA_ENERGY_GAIN = "from using Follow up attack";
    public static final String TALENT_ENERGY_GAIN = "from Talent effect";
    public static final String TRACE_ENERGY_GAIN = "from Trace effect";
    public static final String LIGHTCONE_ENERGY_GAIN = "from Lightcone effect";
    public static final String ATTACKED_ENERGY_GAIN = "from being attacked";
    public static final String TECHNIQUE_ENERGY_GAIN = "from Technique effect";

    @Getter
    protected ActionMetric actionMetric = metricRegistry.register(new ActionMetric("Actions", "Action made"));
    @Getter
    protected CounterMetric<Float> currentEnergy = metricRegistry.register(CounterMetric.newFloatCounter(ENERGY_KEY, "Left over energy"));
    protected CounterMetric<Float> overflowEnergy = metricRegistry.register(CounterMetric.newFloatCounter("overflow"+ENERGY_KEY, "Overflow energy"));

    public final int level;
    public final float baseCritChance = 5.0f;
    public final float baseCritDamage = 50.0f;
    public final float maxEnergy;
    public final int tauntValue;
    public final ElementType elementType;
    public final List<AbstractRelicSetBonus> relicSetBonus;
    public final boolean useTechnique = true;
    @Getter
    protected final Path path;
    protected final int baseHP;
    protected final int baseAtk;
    protected final int baseDef;
    protected final int ultEnergyGain = 5;

    private final SortedMap<Integer, UltGoal<C>> ultGoals = new TreeMap<>();
    private final SortedMap<Integer, TurnGoal<C>> turnGoals = new TreeMap<>();
    private final SortedMap<Integer, EnemyTargetGoal<C>> enemyTargetGoals = new TreeMap<>();
    private final SortedMap<Integer, AllyTargetGoal<C>> allyTargetGoals = new TreeMap<>();
    public boolean usesEnergy = true;
    public float ultCost;
    public AbstractLightcone lightcone;
    public boolean isDPS = false;
    public boolean firstMove = true;
    public boolean hasAttackingUltimate;
    protected int basicEnergyGain = 20;
    protected int skillEnergyGain = 30;

    public AbstractCharacter(String name, int baseHP, int baseAtk, int baseDef, int baseSpeed, int level, ElementType elementType, float maxEnergy, int tauntValue, Path path) {
        super();
        this.name = name;
        this.baseHP = baseHP;
        this.baseAtk = baseAtk;
        this.baseDef = baseDef;
        this.baseSpeed = baseSpeed;
        this.level = level;
        this.elementType = elementType;
        this.maxEnergy = maxEnergy;
        this.ultCost = maxEnergy;
        this.currentEnergy.set(maxEnergy / 2);
        this.tauntValue = tauntValue;
        this.path = path;

        this.powerList = new ArrayList<>();
        this.relicSetBonus = new ArrayList<>();

        this.lightcone = new DefaultLightcone(this);
    }

    public void registerGoal(int priority, AllyTargetGoal<C> allyTargetGoal) {
        if (this.allyTargetGoals.containsKey(priority)) {
            throw new IllegalArgumentException("Priority already exists, " + priority);
        }
        this.allyTargetGoals.put(priority, allyTargetGoal);
    }

    public void registerGoal(int priority, EnemyTargetGoal<C> enemyTargetGoal) {
        if (this.enemyTargetGoals.containsKey(priority)) {
            throw new IllegalArgumentException("Priority already exists, " + priority);
        }
        this.enemyTargetGoals.put(priority, enemyTargetGoal);
    }

    public void clearEnemyTargetGoals() {
        this.enemyTargetGoals.clear();
    }

    public void registerGoal(int priority, UltGoal<C> ultGoal) {
        if (ultGoals.containsKey(priority)) {
            throw new IllegalArgumentException("Priority already exists, " + priority);
        }
        ultGoals.put(priority, ultGoal);
    }

    public void clearUltGoals() {
        ultGoals.clear();
    }

    public void registerGoal(int priority, TurnGoal<C> turnGoal) {
        if (turnGoals.containsKey(priority)) {
            throw new IllegalArgumentException("Priority already exists, " + priority);
        }
        turnGoals.put(priority, turnGoal);
    }

    public void clearTurnGoals() {
        turnGoals.clear();
    }

    protected final AbstractCharacter<?> getAllyTarget() {
        for (AllyTargetGoal<C> goal : this.allyTargetGoals.values()) {
            var optionalAlly = goal.getTarget();
            if (optionalAlly.isPresent()) {
                return optionalAlly.get();
            }
        }

        throw new IllegalStateException("No ally target found");
    }

    protected final AbstractEnemy getTarget(MoveType type) {
        for (EnemyTargetGoal<C> goal : this.enemyTargetGoals.values()) {
            var optionalEnemy = goal.getTarget(type);
            if (optionalEnemy.isPresent()) {
                return optionalEnemy.get();
            }
        }

        throw new IllegalStateException("No enemy target found");
    }

    public final void tryUltimate() {
        if (this.currentEnergy.get() < ultCost) {
            return;
        }

        for (UltGoal<C> ultGoal : this.ultGoals.values()) {
            UltGoal.UltGoalResult result = ultGoal.determineAction();
            switch (result) {
                case DO: {
                    getBattle().addToLog(new UltDecision(this, ultGoal.getClass()));
                    this.ultimateSequence();
                    return;
                }
                case DONT: {
                    return;
                }
                case PASS: {
                    continue;
                }
                default: {
                    throw new IllegalStateException("Unexpected value: " + result);
                }
            }
        }
    }

    protected Attack startAttack() {
        return new Attack(this);
    }

    protected void doAttack(DamageType type, MoveType moveType, BiConsumer<AbstractEnemy, AttackLogic> logic) {
        this.startAttack().handle(type, dh -> dh.logic(this.getTarget(moveType), logic)).execute();
    }

    protected void doAttack(DamageType type, Consumer<DelayAttack> consumer) {
        this.startAttack().handle(type, consumer).execute();
    }

    protected void doAttack(Consumer<DelayAttack> consumer) {
        this.startAttack().handle(consumer).execute();
    }

    @Override
    public final void takeTurn() {
        super.takeTurn();
        this.turnsMetric.increment();

        // TODO: Ulting at the start of the turn makes the dmg higher in some places, but lowers in a few
        // Should investigate why this is happening, and add the needed goals to make it increase at all times
        // Time to read a bunch of logs
        //tryUltimate();
        for (TurnGoal<C> turnGoal : this.turnGoals.values()) {
            TurnGoal.TurnGoalResult result = turnGoal.determineAction();
            switch (result) {
                case BASIC: {
                    getBattle().addToLog(new TurnDecision(this, turnGoal.getClass(), result));
                    this.basicSequence();
                    return;
                }
                case SKILL: {
                    getBattle().addToLog(new TurnDecision(this, turnGoal.getClass(), result));
                    this.skillSequence();
                    return;
                }
                case PASS: {
                    continue;
                }
                default: {
                    throw new IllegalStateException("Unexpected value: " + result);
                }
            }
        }

        throw new IllegalStateException("No valid turn goal found for: " + this.getName());
    }

    protected void skillSequence() {
        this.actionMetric.record(MoveType.SKILL);

        getBattle().addToLog(new DoMove(this, MoveType.SKILL));
        getBattle().useSkillPoint(this, 1);
        increaseEnergy(skillEnergyGain, SKILL_ENERGY_GAIN);
        this.emit(BattleEvents::onUseSkill);
        this.useSkill();
        this.emit(BattleEvents::afterUseSkill);
    }

    protected void basicSequence() {
        this.actionMetric.record(MoveType.BASIC);

        getBattle().addToLog(new DoMove(this, MoveType.BASIC));
        getBattle().generateSkillPoint(this, 1);
        increaseEnergy(basicEnergyGain, BASIC_ENERGY_GAIN);
        this.emit(BattleEvents::onUseBasic);
        this.useBasic();
        this.emit(BattleEvents::afterUseBasic);
    }

    protected void ultimateSequence() {
        this.actionMetric.record(MoveType.ULTIMATE);

        float initialEnergy = this.currentEnergy.get();
        this.currentEnergy.decrease(this.ultCost);
        getBattle().addToLog(new DoMove(this, MoveType.ULTIMATE, initialEnergy, this.currentEnergy.get()));
        increaseEnergy(ultEnergyGain, ULT_ENERGY_GAIN);
        this.emit(BattleEvents::onUseUltimate);
        this.useUltimate();
        this.emit(BattleEvents::afterUseUltimate);
    }

    protected abstract void useSkill();

    protected abstract void useBasic();

    protected abstract void useUltimate();

    public EnemyHitResult hit(EnemyHit hit) {
        increaseEnergy(hit.energy(), ATTACKED_ENERGY_GAIN);
        // TODO: lower HP
        return new EnemyHitResult(hit.dmg());
    }

    public float getFinalAttack() {
        int totalBaseAtk = baseAtk + lightcone.baseAtk;
        float totalBonusAtkPercent = 0;
        float totalBonusFlatAtk = 0;
        for (AbstractPower power : powerList) {
            totalBonusAtkPercent += power.getStat(PowerStat.ATK_PERCENT);
            totalBonusAtkPercent += power.getConditionalAtkBonus(this);
            totalBonusFlatAtk += power.getStat(PowerStat.FLAT_ATK);
        }
        return (totalBaseAtk * (1 + totalBonusAtkPercent / 100) + totalBonusFlatAtk);
    }

    public float getFinalDefense() {
        int totalBaseDef = baseDef + lightcone.baseDef;
        float totalBonusDefPercent = 0;
        float totalBonusFlatDef = 0;
        for (AbstractPower power : powerList) {
            totalBonusDefPercent += power.getStat(PowerStat.DEF_PERCENT);
            totalBonusDefPercent += power.getConditionalDefenseBonus(this);
            totalBonusFlatDef += power.getStat(PowerStat.FLAT_DEF);
        }
        return (totalBaseDef * (1 + totalBonusDefPercent / 100) + totalBonusFlatDef);
    }

    public float getFinalHP() {
        int totalBaseHP = baseHP + lightcone.baseHP;
        float totalBonusHPPercent = 0;
        float totalBonusFlatHP = 0;
        for (AbstractPower power : powerList) {
            totalBonusHPPercent += power.getStat(PowerStat.HP_PERCENT);
            totalBonusFlatHP += power.getStat(PowerStat.FLAT_HP);
        }
        return (totalBaseHP * (1 + totalBonusHPPercent / 100) + totalBonusFlatHP);
    }

    public float getFinalSpeed() {
        float totalBaseSpeed = baseSpeed;
        float totalBonusSpeedPercent = 0;
        float totalBonusFlatSpeed = 0;
        for (AbstractPower power : powerList) {
            totalBonusSpeedPercent += power.getStat(PowerStat.SPEED_PERCENT);
            totalBonusSpeedPercent += power.getConditionalSpeedBoost(this);
            totalBonusFlatSpeed += power.getStat(PowerStat.FLAT_SPEED);
        }
        return (totalBaseSpeed * (1 + totalBonusSpeedPercent / 100) + totalBonusFlatSpeed);
    }

    public float getTotalCritChance() {
        float totalCritChance = baseCritChance;
        for (AbstractPower power : powerList) {
            totalCritChance += power.getStat(PowerStat.CRIT_CHANCE);
        }
        return Math.min(totalCritChance, 100);
    }

    public float getTotalCritDamage() {
        float totalCritDamage = baseCritDamage;
        for (AbstractPower power : powerList) {
            totalCritDamage += power.getStat(PowerStat.CRIT_DAMAGE);
        }
        return totalCritDamage;
    }

    public float getTotalSameElementDamageBonus() {
        float totalSameElementDamageBonus = 0;
        float totalGlobalElementDamageBonus = 0;
        for (AbstractPower power : powerList) {
            totalSameElementDamageBonus += power.getStat(this.elementType.getStatBoost());
            totalGlobalElementDamageBonus += power.getStat(PowerStat.DAMAGE_BONUS);
        }
        return totalSameElementDamageBonus + totalGlobalElementDamageBonus;
    }

    public float getTotalOffElementDamageBonus() {
        float totalGlobalElementDamageBonus = 0;
        for (AbstractPower power : powerList) {
            totalGlobalElementDamageBonus += power.getStat(PowerStat.DAMAGE_BONUS);
        }
        return totalGlobalElementDamageBonus;
    }

    public float getTotalResPen() {
        float totalResPen = 0;
        for (AbstractPower power : powerList) {
            totalResPen += power.getStat(PowerStat.RES_PEN);
        }
        return totalResPen;
    }

    public float getFinalTauntValue() {
        float totalBonusTauntValue = 0;
        for (AbstractPower power : powerList) {
            totalBonusTauntValue += power.getStat(PowerStat.TAUNT_VALUE);
        }
        return (this.tauntValue * (1 + totalBonusTauntValue / 100));
    }

    public float getTotalERR() {
        float totalEnergyRegenBonus = 0;
        for (AbstractPower power : powerList) {
            totalEnergyRegenBonus += power.getStat(PowerStat.ENERGY_REGEN);
            totalEnergyRegenBonus += power.getConditionalERR(this);
        }
        return totalEnergyRegenBonus;
    }

    public float getTotalEHR() {
        float totalEHR = 0;
        for (AbstractPower power : powerList) {
            totalEHR += power.getStat(PowerStat.EFFECT_HIT);
        }
        return totalEHR;
    }

    public float getTotalEffectRes() {
        float totalEffectRes = 0;
        for (AbstractPower power : powerList) {
            totalEffectRes += power.getStat(PowerStat.EFFECT_RES);
        }
        return totalEffectRes;
    }

    public float getTotalBreakEffect() {
        float totalBreakEffect = 0;
        for (AbstractPower power : powerList) {
            totalBreakEffect += power.getStat(PowerStat.BREAK_EFFECT);
            totalBreakEffect += power.getConditionalBreakEffectBonus(this);
        }
        return totalBreakEffect;
    }

    public float getTotalWeaknessBreakEff() {
        float totalWeaknessBreakEff = 0;
        for (AbstractPower power : powerList) {
            totalWeaknessBreakEff += power.getStat(PowerStat.WEAKNESS_BREAK_EFF);
        }
        return totalWeaknessBreakEff;
    }

    public void increaseEnergy(final float amount, boolean ERRAffected, String source) {
        if (!this.usesEnergy) return;
        float initialEnergy = this.currentEnergy.get();
        float totalEnergyRegenBonus = getTotalERR();
        float energyGained = ERRAffected ? amount * (1 + totalEnergyRegenBonus / 100) : amount;

        float overflow = this.currentEnergy.increase(energyGained, this.maxEnergy);
        this.overflowEnergy.increase(overflow);
        this.emit(l -> l.onGainEnergy(energyGained, overflow));
        getBattle().addToLog(new GainEnergy(this, initialEnergy, this.currentEnergy.get(), energyGained, source));
    }

    public void increaseEnergy(float amount, String source) {
        increaseEnergy(amount, true, source);
    }

    @Override
    public float getBaseAV() {
        float speed = getFinalSpeed();
        return 10000 / speed;
    }

    public void EquipLightcone(AbstractLightcone lightcone) {
        this.getListeners().remove(this.lightcone);
        this.lightcone = lightcone;
        this.lightcone.onEquip();
        this.getListeners().add(this.lightcone);
    }

    public void EquipRelicSet(AbstractRelicSetBonus relicSetBonus) {
        this.relicSetBonus.add(relicSetBonus);
        relicSetBonus.onEquip();
        this.getListeners().add(relicSetBonus);
    }

    public boolean lastMove(MoveType move) {
        return this.actionMetric.lastMove(move);
    }

    public boolean lastMoveBefore(MoveType move) {
        return this.actionMetric.lastMoveBefore(move);
    }

    public void useTechnique() {
    }

    public void onWeaknessBreak(AbstractEnemy enemy) {

    }

    public boolean invincible() {
        return false;
    }

    public String getMetrics() {
        return this.statsString() + "\nCombat Metrics\n" + this.metricRegistry.representation() +
                "Left over AV: " + this.leftOverAV();
    }

    private String statsString() {
        String gearString = String.format("Metrics for %s \nLightcone: %s \nRelic Set Bonuses: ", getName(), lightcone);
        gearString += relicSetBonus;
        gearString += String.format("Powers: %s\n", powerList.stream().map(AbstractPower::getName).collect(Collectors.joining("\n\t")));
        return gearString + String.format("\nOut of combat stats \nAtk: %.3f \nDef: %.3f \nHP: %.3f \nSpeed: %.3f \nSame Element Damage Bonus: %.3f \nCrit Chance: %.3f%% \nCrit Damage: %.3f%% \nBreak Effect: %.3f%%", getFinalAttack(), getFinalDefense(), getFinalHP(), getFinalSpeed(), getTotalSameElementDamageBonus(), getTotalCritChance(), getTotalCritDamage(), getTotalBreakEffect());
    }

    // Override if you want some special information
    public String leftOverAV() {
        return String.format("%.2f", getBattle().getActionValueMap().get(this));
    }

    public HashMap<String, String> getCharacterSpecificMetricMap() {
        return new HashMap<>();
    }

    public ArrayList<String> getOrderedCharacterSpecificMetricsKeys() {
        return new ArrayList<>();
    }
}
