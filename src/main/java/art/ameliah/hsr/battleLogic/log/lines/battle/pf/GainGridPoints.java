package art.ameliah.hsr.battleLogic.log.lines.battle.pf;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public record GainGridPoints(int oldAmount, int amount) implements Loggable {
    @Override
    public String asString() {
        return String.format("Grid points changed (%d -> %d)", oldAmount, amount);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
