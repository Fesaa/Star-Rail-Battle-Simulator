package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public record LeftOverAV(float AV) implements Loggable {

    @Override
    public String asString() {
        return "AV until battle ends: " + this.AV;
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
