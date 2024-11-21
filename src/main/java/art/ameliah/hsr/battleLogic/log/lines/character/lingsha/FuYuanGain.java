package art.ameliah.hsr.battleLogic.log.lines.character.lingsha;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public record FuYuanGain(int amount, int initalStack, int fuYuanCurrentHitCount) implements Loggable {

    @Override
    public String asString() {
        return String.format("Fu Yuan gained %,d hits (%,d -> %,d)", amount, initalStack, fuYuanCurrentHitCount);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
