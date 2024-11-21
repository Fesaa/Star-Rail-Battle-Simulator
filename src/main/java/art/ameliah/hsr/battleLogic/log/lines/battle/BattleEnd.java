package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public class BattleEnd implements Loggable {

    private final String reason;

    public BattleEnd(String reason) {
        this.reason = reason;
    }

    public BattleEnd() {
        this("");
    }

    @Override
    public String asString() {
        if (this.reason.isEmpty()) {
            return "Battle Ended";
        }

        return "Battle Ended: " + this.reason;
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
