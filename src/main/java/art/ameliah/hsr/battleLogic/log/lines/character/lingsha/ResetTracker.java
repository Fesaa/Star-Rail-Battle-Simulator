package art.ameliah.hsr.battleLogic.log.lines.character.lingsha;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public class ResetTracker implements Loggable {
    @Override
    public String asString() {
        return "Resetting Lingsha damage tracker due to healing";
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
