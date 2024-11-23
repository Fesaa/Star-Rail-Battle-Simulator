package art.ameliah.hsr.battleLogic;

import art.ameliah.hsr.battleLogic.combat.IAttack;
import art.ameliah.hsr.battleLogic.log.DefaultLogger;
import art.ameliah.hsr.battleLogic.log.LogSupplier;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.battleLogic.log.lines.battle.AdvanceEntity;
import art.ameliah.hsr.battleLogic.log.lines.battle.BattleEnd;
import art.ameliah.hsr.battleLogic.log.lines.battle.CombatStart;
import art.ameliah.hsr.battleLogic.log.lines.battle.DelayEntity;
import art.ameliah.hsr.battleLogic.log.lines.battle.GenerateSkillPoint;
import art.ameliah.hsr.battleLogic.log.lines.battle.LeftOverAV;
import art.ameliah.hsr.battleLogic.log.lines.battle.SpeedAdvanceEntity;
import art.ameliah.hsr.battleLogic.log.lines.battle.SpeedDelayEntity;
import art.ameliah.hsr.battleLogic.log.lines.battle.TriggerTechnique;
import art.ameliah.hsr.battleLogic.log.lines.battle.TurnStart;
import art.ameliah.hsr.battleLogic.log.lines.battle.UseSkillPoint;
import art.ameliah.hsr.battleLogic.log.lines.metrics.BattleMetrics;
import art.ameliah.hsr.battleLogic.log.lines.metrics.EnemyMetrics;
import art.ameliah.hsr.battleLogic.log.lines.metrics.FinalDmgMetrics;
import art.ameliah.hsr.battleLogic.log.lines.metrics.PostCombatPlayerMetrics;
import art.ameliah.hsr.battleLogic.log.lines.metrics.PreCombatPlayerMetrics;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.march.SwordMarch;
import art.ameliah.hsr.characters.yunli.Yunli;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Battle implements IBattle {

    protected List<AbstractCharacter<?>> playerTeam = new ArrayList<>();
    protected List<AbstractEnemy> enemyTeam = new ArrayList<>();
    protected Deque<IAttack> queue = new LinkedList<>();
    protected boolean activeAttack = false;

    private final BattleHelpers battleHelpers;
    @Getter
    private Logger logger;

    public final int INITIAL_SKILL_POINTS = 3;
    public int numSkillPoints = INITIAL_SKILL_POINTS;
    public int MAX_SKILL_POINTS = 5;
    public int totalPlayerDamage;
    public float finalDPAV;
    public int totalSkillPointsUsed = 0;
    public int totalSkillPointsGenerated = 0;
    public String log = "";
    public float initialBattleLength;
    public float battleLength;
    public AbstractEntity currentUnit;
    public boolean usedEntryTechnique = false;
    public boolean isInCombat = false;
    public boolean lessMetrics = false;
    public int actionForwardPriorityCounter = AbstractEntity.SPEED_PRIORITY_DEFAULT;

    public HashMap<AbstractCharacter<?>, Float> damageContributionMap;
    public HashMap<AbstractCharacter<?>, Float> damageContributionMapPercent;
    public HashMap<AbstractEntity, Float> actionValueMap;

    protected static final long seed = 154172837382L;
    public final Random enemyMoveRng = new Random(seed);
    public final Random enemyTargetRng = new Random(seed);
    public final Random critChanceRng = new Random(seed);
    public final Random getRandomEnemyRng = new Random(seed);
    public final Random procChanceRng = new Random(seed);
    public final Random gambleChanceRng = new Random(seed);
    public final Random qpqRng = new Random(seed);
    public final Random milkyWayRng = new Random(seed);
    public final Random weaveEffectRng = new Random(seed);
    public final Random aetherRng = new Random(seed);
    public final Random enemyEHRRng = new Random(seed);

    public Battle() {
        this.battleHelpers = new BattleHelpers(this);
        this.logger = new DefaultLogger(this);
    }

    public Battle(LogSupplier logger) {
        this.battleHelpers = new BattleHelpers(this);
        this.logger = logger.get(this);
    }

    public void setLogger(LogSupplier logger) {
        this.logger = logger.get(this);
    }

    @Override
    public Deque<IAttack> attackQueue() {
        return this.queue;
    }

    @Override
    public boolean isAttacking() {
        return this.activeAttack;
    }

    @Override
    public void setAttacking(boolean attacking) {
        this.activeAttack = attacking;
    }

    @Override
    public void setPlayerTeam(List<AbstractCharacter<?>> playerTeam) {
        this.playerTeam = playerTeam;
        this.playerTeam.forEach(character -> character.setBattle(this));
    }

    @Override
    public void setEnemyTeam(List<AbstractEnemy> enemyTeam) {
        this.enemyTeam = new ArrayList<>(enemyTeam);
        this.enemyTeam.forEach(enemy -> enemy.setBattle(this));
    }

    @Override
    public AbstractEnemy getRandomEnemy() {
        return this.enemyTeam.get(this.getRandomEnemyIdx());
    }

    @Override
    public int getRandomEnemyIdx() {
        return getRandomEnemyRng.nextInt(this.enemyTeam.size());
    }

    @Override
    public void enemyCallback(int idx, Consumer<AbstractEnemy> callback) {
        if (idx < 0 || idx >= enemyTeam.size()) {
            return;
        }
        AbstractEnemy target = this.enemyTeam.get(idx);
        if (target != null) {
            callback.accept(target);
        }
    }

    @Override
    public final void removeEnemy(AbstractEnemy enemy) {
        int idx = this.enemyTeam.indexOf(enemy);
        if (idx == -1) {
            throw new IllegalStateException("Trying to remove enemy that's not in battle");
        }
        this.enemyTeam.remove(idx);
        this.actionValueMap.remove(enemy);

        this.onEnemyRemove(enemy, idx);
    }

    @Override
    public void addEnemyAt(AbstractEnemy enemy, int idx, float initialAA) {
        this.enemyTeam.add(idx, enemy);
        enemy.setBattle(this);

        this.actionValueMap.put(enemy, enemy.getBaseAV());
        if (initialAA > 0) {
            this.AdvanceEntity(enemy, initialAA);
        }

        enemy.emit(BattleEvents::onCombatStart);
    }

    /**
     * Override to implement custom enemy leave actions.
     * I.E. For waves, pf, etc...
     * <p>
     * Default behavior, end battle if everyone dead
     */
    protected void onEnemyRemove(AbstractEnemy enemy, int idx) {
        if (!this.enemyTeam.isEmpty()) {
            return;
        }

        throw new ForceBattleEnd();
    }

    @Override
    public AbstractEntity getCurrentUnit() {
        return this.currentUnit;
    }

    @Override
    public void setCurrentUnit(AbstractEntity entity) {
        this.currentUnit = entity;
        this.actionValueMap.put(entity, 0.0F);
    }

    @Override
    public AbstractEntity getNextUnit(int index) {
        if (index >= this.actionValueMap.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for actionValueMap size " + this.actionValueMap.size());
        }

        return this.actionValueMap
                .entrySet()
                .stream()
                .sorted((e1, e2) -> {
                    if (e1.getValue().equals(e2.getValue())) {
                        return Integer.compare(e1.getKey().speedPriority, e2.getKey().speedPriority);
                    }
                    return Float.compare(e1.getValue(), e2.getValue());
                })
                .toList()
                .get(index)
                .getKey();
    }

    @Override
    public boolean isAboutToEnd() {
        AbstractEntity next = this.getNextUnit(0);
        return actionValueMap.get(next) > battleLength;
    }

    @Override
    public boolean inCombat() {
        return this.isInCombat;
    }

    @Override
    public void updateContribution(AbstractCharacter<?> character, float damageContribution) {
        if (damageContributionMap.containsKey(character)) {
            float existingTotal = damageContributionMap.get(character);
            float updatedTotal = existingTotal + damageContribution;
            damageContributionMap.put(character, updatedTotal);
        } else {
            damageContributionMap.put(character, damageContribution);
        }
    }

    @Override
    public void increaseTotalPlayerDmg(float dmg) {
        totalPlayerDamage += (int)dmg;
    }

    @Override
    public float initialLength() {
        return this.initialBattleLength;
    }

    @Override
    public float battleLength() {
        return this.battleLength;
    }

    @Override
    public BattleHelpers getHelper() {
        return this.battleHelpers;
    }

    @Override
    public void useSkillPoint(AbstractCharacter<?> character, int amount) {
        int initialSkillPoints = numSkillPoints;
        numSkillPoints -= amount;
        totalSkillPointsUsed += amount;
        addToLog(new UseSkillPoint(character, amount, initialSkillPoints, numSkillPoints));
        if (numSkillPoints < 0) {
            throw new RuntimeException("ERROR - SKILL POINTS WENT NEGATIVE");
        }
    }

    @Override
    public void generateSkillPoint(AbstractCharacter<?> character, int amount) {
        int initialSkillPoints = numSkillPoints;
        numSkillPoints += amount;
        totalSkillPointsGenerated += amount;
        if (numSkillPoints > MAX_SKILL_POINTS) {
            numSkillPoints = MAX_SKILL_POINTS;
        }
        addToLog(new GenerateSkillPoint(character, amount, initialSkillPoints, numSkillPoints));
    }

    @Override
    public void increaseMaxSkillPoints(int maxSkillPoints) {
        MAX_SKILL_POINTS += maxSkillPoints;
    }

    @Override
    public int getSkillPoints() {
        return this.numSkillPoints;
    }

    /**
     * Method to override for extended function if enemies aren't present at their construction
     */
    public void onStart() {}

    @Override
    public void Start(float initialLength) {
        damageContributionMap = new HashMap<>();
        damageContributionMapPercent = new HashMap<>();
        numSkillPoints = INITIAL_SKILL_POINTS;
        actionValueMap = new HashMap<>();
        usedEntryTechnique = false;

        this.onStart();
        initialBattleLength = initialLength;
        this.battleLength = initialLength;
        totalPlayerDamage = 0;
        addToLog(new CombatStart());
        this.playerTeam.forEach(c -> addToLog(new PreCombatPlayerMetrics(c)));

        for (AbstractEnemy enemy : enemyTeam) {
            actionValueMap.put(enemy, enemy.getBaseAV());
            enemy.emit(BattleEvents::onCombatStart);
        }
        for (AbstractCharacter<?> character : playerTeam) {
            character.generateStatsString();
            character.generateStatsReport();
        }
        isInCombat = true;
        for (AbstractCharacter<?> character : playerTeam) {
            actionValueMap.put(character, character.getBaseAV());
            character.emit(BattleEvents::onCombatStart);
        }

        addToLog(new TriggerTechnique(this.playerTeam
                .stream()
                .filter(c -> c.useTechnique)
                .collect(Collectors.toList())));
        for (AbstractCharacter<?> character : playerTeam) {
            if (character.useTechnique) {
                character.useTechnique();
            }
        }

        Yunli yunli = getYunli();
        SwordMarch march = getSwordMarch();

        while (battleLength > 0 && this.isInCombat) {
            try {
                this.battleLoop(yunli, march); // TODO: THIS SHOULD NOT NEED CHARACTERS
            } catch (ForceBattleEnd forceBattleEnd) {
                this.isInCombat = false;
                String reason = forceBattleEnd.getMessage();
                if (reason == null || reason.isEmpty()) {
                    addToLog(new BattleEnd("Forcefully ended"));
                } else {
                    addToLog(new BattleEnd(reason));
                }
                break;
            }
        }

        calcPercentContribution();
        this.generateMetrics();
    }

    private void battleLoop(Yunli yunli, SwordMarch march) {
        addToLog(new LeftOverAV(this.battleLength));
        currentUnit = this.getNextUnit(0);
        float nextAV = actionValueMap.get(currentUnit);
        if (nextAV > battleLength) {
            for (Map.Entry<AbstractEntity,Float> entry : actionValueMap.entrySet()) {
                float newAV = entry.getValue() - battleLength;
                entry.setValue(newAV);
            }
            addToLog(new BattleEnd());
            isInCombat = false;
            return;
        }
        // TODO: Find a way to remove Yunli logic from here
        // Ideally the battle loop does not care about the specifics of the characters
        if (yunli != null && yunli.currentEnergy >= yunli.ultCost) {
            yunli.tryUltimate();
            if (march != null && march.chargeCount >= march.chargeThreshold) {
                currentUnit = march;
                nextAV = actionValueMap.get(currentUnit);
            }
        }
        battleLength -= nextAV;
        for (Map.Entry<AbstractEntity,Float> entry : actionValueMap.entrySet()) {
            float newAV = entry.getValue() - nextAV;
            entry.setValue(newAV);
        }
        addToLog(new TurnStart(currentUnit, this.getActionValueUsed() ,actionValueMap));


        currentUnit.emit(BattleEvents::onTurnStart);
        // need the AV reset to be after onTurnStart is emitted so Robin's AV is set properly after Concerto ends
        if (!(currentUnit instanceof AbstractSummon<?>)) {
            if (actionValueMap.get(currentUnit) <= 0) {
                actionValueMap.put(currentUnit, currentUnit.getBaseAV());
            }
        }
        currentUnit.takeTurn();
        currentUnit.emit(BattleEvents::onEndTurn);

        if (yunli != null && yunli.isParrying) {
            yunli.useSlash(getRandomEnemy());
        }

        getPlayers().stream()
                .filter(p -> !(p instanceof Yunli))
                .forEach(AbstractCharacter::tryUltimate);
        getPlayers().stream()
                .filter(p -> !(p instanceof Yunli))
                .forEach(AbstractCharacter::tryUltimate);

        if (currentUnit instanceof AbstractSummon) {
            actionValueMap.put(currentUnit, currentUnit.getBaseAV());
        }

        getPlayers().stream()
                .filter(p -> !(p instanceof Yunli))
                .forEach(AbstractCharacter::tryUltimate);
    }

    private void generateMetrics() {
        this.playerTeam.forEach(p -> addToLog(new PostCombatPlayerMetrics(p, lessMetrics)));
        if (!lessMetrics) {
            this.enemyTeam.forEach(e -> addToLog(new EnemyMetrics(e)));
        }
        float usedAV = this.initialBattleLength - battleLength;
        finalDPAV = (float)totalPlayerDamage / usedAV;
        addToLog(new BattleMetrics(this));
        addToLog(new FinalDmgMetrics(this));
    }

    public void calcPercentContribution() {
        for (AbstractCharacter<?> character : playerTeam) {
            Float damage = damageContributionMap.get(character);
            if (damage == null) {
                damageContributionMap.put(character, 0.0f);
                damageContributionMapPercent.put(character, 0.0f);
            } else {
                float percent = damage / totalPlayerDamage * 100;
                damageContributionMapPercent.put(character, percent);
            }
        }
    }

    private Yunli getYunli() {
        for (AbstractCharacter<?> character : playerTeam) {
            if (character instanceof Yunli) {
                return (Yunli) character;
            }
        }
        return null;
    }

    private SwordMarch getSwordMarch() {
        for (AbstractCharacter<?> character : playerTeam) {
            if (character instanceof SwordMarch) {
                return (SwordMarch) character;
            }
        }
        return null;
    }

    @Override
    public boolean getLessMetrics() {
        return this.lessMetrics;
    }

    @Override
    public boolean usedEntryTechnique() {
        return this.usedEntryTechnique;
    }

    @Override
    public void setUsedEntryTechnique(boolean usedEntryTechnique) {
        this.usedEntryTechnique = usedEntryTechnique;
    }

    @Override
    public List<AbstractCharacter<?>> getPlayers() {
        return this.playerTeam;
    }

    @Override
    public boolean hasCharacter(String name) {
        for (AbstractCharacter<?> character : playerTeam) {
            if (character.name.equals(name)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public AbstractCharacter<?> getCharacter(String name) {
        for (AbstractCharacter<?> character : playerTeam) {
            if (character.name.equals(name)) {
                return character;
            }
        }
        return null;
    }

    @Override
    public AbstractCharacter<?> getCharacter(int index) {
        if (index < 0 || index >= playerTeam.size()) {
            return null;
        }
        return playerTeam.get(index);
    }

    @Override
    public void characterCallback(String name, Consumer<AbstractCharacter<?>> callback) {
        AbstractCharacter<?> character = this.getCharacter(name);
        if (character != null  && callback != null) {
            callback.accept(character);
        }
    }

    @Override
    public void characterCallback(int idx, Consumer<AbstractCharacter<?>> callback) {
        AbstractCharacter<?> character = this.getCharacter(idx);
        if (character != null && callback != null) {
            callback.accept(character);
        }
    }

    @Override
    public List<AbstractEnemy> getEnemies() {
        return this.enemyTeam;
    }

    @Override
    public AbstractEnemy getMiddleEnemy() {
        AbstractEnemy enemy;
        if (this.enemyTeam.size() >= 3) {
            int middleIndex = this.enemyTeam.size() / 2;
            enemy = this.enemyTeam.get(middleIndex);
        } else {
            enemy = this.enemyTeam.getFirst();
        }
        return enemy;
    }

    @Override
    public AbstractEnemy getEnemyWithHighestHP() {
        AbstractEnemy enemy = null;
        for (AbstractEnemy e : this.getEnemies()) {
            if (enemy == null || e.getCurrentHp() > enemy.getCurrentHp()) {
                enemy = e;
            }
        }

        return enemy;
    }

    @Override
    public void addToLog(Loggable addition) {
        this.logger.handle(addition);
    }

    @Override
    public HashMap<AbstractCharacter<?>, Float> getDamageContributionMap() {
        return this.damageContributionMap;
    }

    @Override
    public HashMap<AbstractCharacter<?>, Float> getDamageContributionMapPercent() {
        return this.damageContributionMapPercent;
    }

    @Override
    public HashMap<AbstractEntity, Float> getActionValueMap() {
        return this.actionValueMap;
    }

    @Override
    public int getTotalPlayerDmg() {
        return this.totalPlayerDamage;
    }

    @Override
    public float getActionValueUsed() {
        return this.initialBattleLength - this.battleLength;
    }

    @Override
    public float getFinalDPAV() {
        return this.finalDPAV;
    }

    @Override
    public int getTotalSkillPointsUsed() {
        return this.totalSkillPointsUsed;
    }

    @Override
    public int getTotalSkillPointsGenerated() {
        return this.totalSkillPointsGenerated;
    }

    @Override
    public void AdvanceEntity(AbstractEntity entity, float advanceAmount) {
        for (Map.Entry<AbstractEntity,Float> entry : actionValueMap.entrySet()) {
            if (entry.getKey() == entity) {
                float baseAV = entity.getBaseAV();
                float AVDecrease = baseAV * (advanceAmount / 100);
                float originalAV = entry.getValue();
                float newAV = originalAV - AVDecrease;
                if (newAV < 0) {
                    newAV = 0;
                }
                entry.setValue(newAV);
                actionForwardPriorityCounter--;
                entity.speedPriority = actionForwardPriorityCounter;
                addToLog(new AdvanceEntity(entity, advanceAmount, originalAV, newAV));
            }
        }
    }

    @Override
    public void DelayEntity(AbstractEntity entity, float delayAmount) {
        for (Map.Entry<AbstractEntity,Float> entry : actionValueMap.entrySet()) {
            if (entry.getKey() == entity) {
                float baseAV = entity.getBaseAV();
                float AVIncrease = baseAV * (delayAmount / 100);
                float originalAV = entry.getValue();
                float newAV = originalAV + AVIncrease;
                entry.setValue(newAV);
                addToLog(new DelayEntity(entity, delayAmount, originalAV, newAV));
            }
        }
    }

    @Override
    public void IncreaseSpeed(AbstractEntity entity, AbstractPower speedPower) {
        float baseAV = entity.getBaseAV();
        Float currAV = actionValueMap.get(entity);
        if (currAV == null) {
            entity.addPower(speedPower);
            return;
        }
        float percentToNextAction = (baseAV - currAV) / baseAV;

        entity.addPower(speedPower);
        float newBaseAV = entity.getBaseAV();
        float newCurrAV = newBaseAV - (percentToNextAction * newBaseAV);
        actionValueMap.put(entity, newCurrAV);

        addToLog(new SpeedAdvanceEntity(entity, currAV, newCurrAV));
    }

    @Override
    public void DecreaseSpeed(AbstractEntity entity, AbstractPower speedPower) {
        float baseAV = entity.getBaseAV();
        Float currAV = actionValueMap.get(entity);
        if (currAV == null) {
            entity.removePower(speedPower);
            return;
        }
        float percentToNextAction = (baseAV - currAV) / baseAV;

        entity.removePower(speedPower);
        float newBaseAV = entity.getBaseAV();
        float newCurrAV = newBaseAV - (percentToNextAction * newBaseAV);
        actionValueMap.put(entity, newCurrAV);

        addToLog(new SpeedDelayEntity(entity, currAV, newCurrAV));
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public Random getEnemyMoveRng() {
        return enemyMoveRng;
    }

    @Override
    public Random getEnemyTargetRng() {
        return enemyTargetRng;
    }

    @Override
    public Random getCritChanceRng() {
        return critChanceRng;
    }

    @Override
    public Random getGetRandomEnemyRng() {
        return getRandomEnemyRng;
    }

    @Override
    public Random getProcChanceRng() {
        return procChanceRng;
    }

    @Override
    public Random getGambleChanceRng() {
        return gambleChanceRng;
    }

    @Override
    public Random getQpqRng() {
        return qpqRng;
    }

    @Override
    public Random getMilkyWayRng() {
        return milkyWayRng;
    }

    @Override
    public Random getWeaveEffectRng() {
        return weaveEffectRng;
    }

    @Override
    public Random getAetherRng() {
        return aetherRng;
    }

    @Override
    public Random getEnemyEHRRng() {
        return enemyEHRRng;
    }

    public static class ForceBattleEnd extends RuntimeException {
        public ForceBattleEnd() {
            super();
        }

        public ForceBattleEnd(String message) {
            super(message);
        }
    }
}
