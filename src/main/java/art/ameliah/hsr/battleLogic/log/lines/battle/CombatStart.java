package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public class CombatStart implements Loggable {
    @Override
    public String asString() {
        return "Combat Start";
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
