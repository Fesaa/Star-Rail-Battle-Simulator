package art.ameliah.hsr.game.moc.outofhome;

import art.ameliah.hsr.battleLogic.wave.moc.MocBattle;
import art.ameliah.hsr.battleLogic.wave.moc.MocWave;
import art.ameliah.hsr.enemies.game.amphoreus.NoontideGryphon;
import art.ameliah.hsr.enemies.game.stellaronhunters.Kafka;

public class Side2 extends MocBattle {
    public Side2() {
        super(new Turbulence());

        this.addWave(new MocWave(
                new NoontideGryphon(1256968)
        ));
        this.addWave(new MocWave(
                new Kafka(2025115)
        ));
    }
}
