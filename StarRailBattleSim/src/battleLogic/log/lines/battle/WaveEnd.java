package battleLogic.log.lines.battle;

import battleLogic.log.Loggable;
import battleLogic.log.Logger;
import battleLogic.wave.Wave;

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
