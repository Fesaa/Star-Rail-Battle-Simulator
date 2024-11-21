package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public class SpeedAdvanceEntity implements Loggable {

    public final AbstractEntity entity;
    public final float from;
    public final float to;

    public SpeedAdvanceEntity(AbstractEntity entity, float from, float to) {
        this.entity = entity;
        this.from = from;
        this.to = to;
    }

    @Override
    public String asString() {
        return String.format("%s advanced by speed increase (%.3f -> %.3f)", this.entity.name, this.from, this.to);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
