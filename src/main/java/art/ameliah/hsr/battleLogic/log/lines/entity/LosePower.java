package art.ameliah.hsr.battleLogic.log.lines.entity;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.powers.AbstractPower;

public record LosePower(AbstractEntity entity, AbstractPower power) implements Loggable {

    @Override
    public String asString() {
        return this.entity.getName() + " lost " + this.power.getName();
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }

}
