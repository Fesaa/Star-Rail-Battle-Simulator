package art.ameliah.hsr.battleLogic.log.lines.enemy;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.enemies.AbstractEnemy;

public class ReduceToughness implements Loggable {

    public final AbstractEnemy enemy;
    public final float tDelta;
    public final float from;
    public final float to;

    public ReduceToughness(AbstractEnemy enemy, float tDelta, float from, float to) {
        this.enemy = enemy;
        this.tDelta = tDelta;
        this.from = from;
        this.to = to;
    }

    @Override
    public String asString() {
        return String.format("%s's toughness reduced by %.3f (%.3f -> %.3f)", this.enemy.name, this.tDelta, this.from, this.to);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
