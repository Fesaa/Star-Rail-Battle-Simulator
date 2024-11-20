package art.ameliah.hsr.battleLogic.log.lines.entity;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.powers.AbstractPower;

public class GainPower implements Loggable {

    public final AbstractEntity entity;
    public final AbstractPower power;

    public GainPower(AbstractEntity entity, AbstractPower power) {
        this.entity = entity;
        this.power = power;
    }

    @Override
    public String asString() {
        return this.entity.name + " gained " + this.power.name;
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
