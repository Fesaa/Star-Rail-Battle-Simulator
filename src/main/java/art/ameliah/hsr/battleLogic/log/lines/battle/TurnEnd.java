package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.enemies.AbstractEnemy;

import java.util.List;

public record TurnEnd(AbstractEntity entity, List<AbstractEnemy> enemies) implements Loggable {
    @Override
    public String asString() {
        return String.format("%s's turn has ended %s", this.entity.getName(), this.enemies);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
