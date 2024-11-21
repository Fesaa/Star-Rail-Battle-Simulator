package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public record AdvanceEntity(AbstractEntity entity, float avDelta, float from, float to) implements Loggable {

    @Override
    public String asString() {
        return String.format("%s advanced by %.1f%% (%.3f -> %.3f)", this.entity.name, this.avDelta, this.from, this.to);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
