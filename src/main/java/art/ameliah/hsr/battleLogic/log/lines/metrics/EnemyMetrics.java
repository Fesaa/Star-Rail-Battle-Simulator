package art.ameliah.hsr.battleLogic.log.lines.metrics;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.enemies.AbstractEnemy;

public record EnemyMetrics(AbstractEnemy enemy) implements Loggable {

    @Override
    public String asString() {
        return this.enemy.getMetrics();
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
