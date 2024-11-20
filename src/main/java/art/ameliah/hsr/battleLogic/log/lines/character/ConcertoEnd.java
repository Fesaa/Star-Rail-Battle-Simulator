package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public class ConcertoEnd implements Loggable {
    @Override
    public String asString() {
        return "Concerto ends, it's now Robin's turn";
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
