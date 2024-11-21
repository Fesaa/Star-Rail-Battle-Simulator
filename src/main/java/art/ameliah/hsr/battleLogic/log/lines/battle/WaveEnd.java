package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.battleLogic.wave.Wave;

public class WaveEnd implements Loggable {

    public final Wave wave;

    public WaveEnd(Wave wave) {
        this.wave = wave;
    }

    @Override
    public String asString() {
        return String.format("Wave %s has ended", wave.Identifier());
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
