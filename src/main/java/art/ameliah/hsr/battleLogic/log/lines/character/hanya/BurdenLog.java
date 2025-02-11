package art.ameliah.hsr.battleLogic.log.lines.character.hanya;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public record BurdenLog(int hitCount, int hitsToTrigger) implements Loggable {

    @Override
    public String asString() {
        return String.format("Burden is at %,d/%,d hits", hitCount, hitsToTrigger);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
