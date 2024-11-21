package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.battleLogic.wave.Wave;

public class WaveStart implements Loggable {

    public final Wave wave;

    public WaveStart(Wave wave) {
        this.wave = wave;
    }

    @Override
    public String asString() {
        return String.format("Wave %s has started", wave.Identifier());
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
