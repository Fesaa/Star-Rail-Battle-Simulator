package art.ameliah.hsr.battleLogic;

import art.ameliah.hsr.battleLogic.combat.IAttack;
import art.ameliah.hsr.battleLogic.combat.result.HitResult;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.metrics.MetricRegistry;
import art.ameliah.hsr.powers.AbstractPower;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public interface IBattle {

    MetricRegistry getMetricRegistry();

    String prefix();

    void addToQueue(IAttack attack, boolean forceFirst);

    boolean isAttacking();

    void setAttacking(boolean attacking);

    void setPlayerTeam(List<AbstractCharacter<?>> players);

    void setEnemyTeam(List<AbstractEnemy> enemies);

    void Start(float AV);

    String metrics();

    boolean getLessMetrics();

    boolean usedEntryTechnique();

    void setUsedEntryTechnique(boolean usedEntryTechnique);

    void removeEntity(AbstractEntity entity);

    List<AbstractCharacter<?>> getPlayers();

    boolean hasCharacter(String name);

    void registerForPlayers(Consumer<AbstractCharacter<?>> consumer);

    void registerForEnemy(Consumer<AbstractEnemy> consumer);

    void doOnceForPlayers(Consumer<AbstractCharacter<?>> consumer);

    void doOnceForEnemy(Consumer<AbstractEnemy> consumer);

    int playerSize();

    int enemiesSize();

    AbstractCharacter<?> getCharacter(String name);

    AbstractCharacter<?> getCharacter(int index);

    void characterCallback(String name, Consumer<AbstractCharacter<?>> callback);

    /**
     * Consumes the callback if the character at index idx exists
     *
     * @param idx      The index to get the character from
     * @param callback The callback to consume
     */
    void characterCallback(int idx, Consumer<AbstractCharacter<?>> callback);

    List<AbstractEnemy> getEnemies();

    AbstractEnemy getMiddleEnemy();

    AbstractEnemy getEnemyWithHighestHP();

    AbstractEnemy getRandomEnemy();

    int getRandomEnemyIdx();

    void enemyCallback(int idx, Consumer<AbstractEnemy> callback);

    void removeEnemy(AbstractEnemy enemy);

    void addPlayerAt(AbstractCharacter<?> ally, int idx, float initialAA);

    default void addPlayerAt(AbstractCharacter<?> ally, int idx) {
        this.addPlayerAt(ally, idx, 0);
    }

    default void addAlly(AbstractCharacter<?> ally, float initialAA) {
        this.addPlayerAt(ally, this.playerSize(), initialAA);
    }

    default void addAlly(AbstractCharacter<?> ally) {
        this.addPlayerAt(ally, this.playerSize(), 0);
    }

    void addEnemyAt(AbstractEnemy enemy, int idx, float initialAA);

    default void addEnemyAt(AbstractEnemy enemy, int idx) {
        this.addEnemyAt(enemy, idx, 0);
    }

    default void addEnemy(AbstractEnemy enemy, float initialAA) {
        this.addEnemyAt(enemy, enemiesSize(), initialAA);
    }

    default void addEnemy(AbstractEnemy enemy) {
        this.addEnemy(enemy, 0);
    }

    AbstractEntity getCurrentUnit();

    void setCurrentUnit(AbstractEntity entity);

    AbstractEntity getNextUnit(int index);

    boolean isAboutToEnd();

    boolean inCombat();

    void AdvanceEntity(AbstractEntity entity, float advanceAmount);

    void DelayEntity(AbstractEntity entity, float delayAmount);

    void IncreaseSpeed(AbstractEntity entity, AbstractPower speedPower);

    void DecreaseSpeed(AbstractEntity entity, AbstractPower speedPower);

    void useSkillPoint(AbstractCharacter<?> character, int amount);

    void generateSkillPoint(AbstractCharacter<?> character, int amount);

    void increaseMaxSkillPoints(int maxSkillPoints);

    int getSkillPoints();

    void addToLog(Loggable addition);

    HashMap<AbstractEntity, Float> getActionValueMap();

    int getTotalPlayerDmg();

    float getActionValueUsed();

    int getTotalSkillPointsUsed();

    int getTotalSkillPointsGenerated();

    void updateContribution(BattleParticipant source, HitResult hit);

    void increaseTotalPlayerDmg(float dmg);

    float initialLength();

    float battleLength();

    int maxEnemiesOnField();

    long getSeed();

    Random getEnemyMoveRng();

    Random getEnemyTargetRng();

    Random getCritChanceRng();

    Random getGetRandomEnemyRng();

    Random getProcChanceRng();

    Random getGambleChanceRng();

    Random getQpqRng();

    Random getMilkyWayRng();

    Random getWeaveEffectRng();

    Random getAetherRng();

    Random getEnemyEHRRng();


}
