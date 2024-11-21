package art.ameliah.hsr.battleLogic.log.lines.enemy;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public class RuanMeiDelay implements Loggable {
    @Override
    public String asString() {
        return "Ruan Mei Ult Delay Triggered";
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
