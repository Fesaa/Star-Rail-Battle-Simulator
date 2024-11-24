package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public record TurnEnd(AbstractEntity entity) implements Loggable {
    @Override
    public String asString() {
        return String.format("%s's turn has ended", this.entity.name);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
