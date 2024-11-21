package art.ameliah.hsr.battleLogic.log.lines.entity;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.powers.AbstractPower;

public record StackPower(AbstractEntity entity, AbstractPower power, int stack) implements Loggable {

    @Override
    public String asString() {
        return this.entity.name + " stacked " + this.power.name + " to " + this.stack;
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
