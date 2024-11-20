package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public class LeftOverAV implements Loggable {

    public final float AV;

    public LeftOverAV(float AV) {
        this.AV = AV;
    }

    @Override
    public String asString() {
        return "AV until battle ends: " + this.AV;
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
