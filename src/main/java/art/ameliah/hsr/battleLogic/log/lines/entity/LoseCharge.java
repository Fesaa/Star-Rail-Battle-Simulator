package art.ameliah.hsr.battleLogic.log.lines.entity;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public record LoseCharge(AbstractEntity character, int amount, int initialCharge, int chargeCount) implements Loggable {

    @Override
    public String asString() {
        return String.format("%s loses %,d Charge (%,d -> %,d)", character.name, amount, initialCharge, chargeCount);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
