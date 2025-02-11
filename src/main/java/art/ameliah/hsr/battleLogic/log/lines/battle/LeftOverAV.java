package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public record LeftOverAV(IBattle battle) implements Loggable {

    @Override
    public String asString() {
        return "AV until battle ends: " + this.battle.battleLength() + " " + this.battle.getEnemies();
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
