package art.ameliah.hsr.battleLogic.log.lines.metrics;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.enemies.AbstractEnemy;

public class EnemyMetrics implements Loggable {

    public final AbstractEnemy enemy;
    public final float speed;
    public final int totalAttacks;
    public final int singleTargetAttacks;
    public final int blastAttacks;
    public final int AoEAttacks;
    public final int weaknessBroken;

    public EnemyMetrics(AbstractEnemy enemy) {
        this.enemy = enemy;

        this.speed = enemy.getBaseSpeed();
        this.totalAttacks = enemy.numAttacksMetric;
        this.singleTargetAttacks = enemy.numSingleTargetMetric;
        this.blastAttacks = enemy.numBlastMetric;
        this.AoEAttacks = enemy.numAoEMetric;
        this.weaknessBroken = enemy.timesBrokenMetric;
    }

    @Override
    public String asString() {
        return String.format("Metrics for %s with %f speed \nTotal attacks: %,d \nSingle-target attacks: %,d \nBlast attacks: %,d \nAoE attacks: %,d \nWeakness Broken: %,d", enemy.getName(), speed, totalAttacks, singleTargetAttacks, blastAttacks, AoEAttacks, weaknessBroken)
                + this.enemy.getMetricRegistry().representation();
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
