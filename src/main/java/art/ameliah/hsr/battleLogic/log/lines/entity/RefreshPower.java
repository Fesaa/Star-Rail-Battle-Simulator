package art.ameliah.hsr.battleLogic.log.lines.entity;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.powers.AbstractPower;

public record RefreshPower(AbstractEntity entity, AbstractPower power, int turns) implements Loggable {

    @Override
    public String asString() {
        return this.entity + " refreshed " + this.power.name + " (" + this.turns + " turn(s))";
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
