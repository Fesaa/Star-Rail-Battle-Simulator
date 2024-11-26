package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public record SpeedAdvanceEntity(AbstractEntity entity, float from, float to) implements Loggable {

    @Override
    public String asString() {
        return String.format("%s advanced by speed increase (%.3f -> %.3f)", this.entity.getName(), this.from, this.to);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
