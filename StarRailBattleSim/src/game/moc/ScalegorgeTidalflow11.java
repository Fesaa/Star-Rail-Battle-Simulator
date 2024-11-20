package game.moc;


import battleLogic.wave.moc.Moc;
import battleLogic.wave.moc.MocBattle;
import battleLogic.wave.moc.MocTurbulence;
import battleLogic.wave.moc.MocWave;
import characters.AbstractCharacter;
import enemies.game.AurumatonSpectralEnvoy;
import enemies.game.GuardianShadow;
import enemies.game.Kafka;

import java.util.List;

public class ScalegorgeTidalflow11 extends Moc {

    public ScalegorgeTidalflow11(List<AbstractCharacter<?>> firstTeam, List<AbstractCharacter<?>> secondTeam) {
        super(new FirstHalf(firstTeam), new SecondHalf(secondTeam));
    }

    public static class FirstHalf extends MocBattle {

        public FirstHalf(List<AbstractCharacter<?>> players) {
            super(new ScalegorgeTidalflowTubalance());
            this.setPlayerTeam(players);
            this.addWave(new MocWave(new AurumatonSpectralEnvoy(), new GuardianShadow()));
            this.addWave(new MocWave(new GuardianShadow(), new Kafka()));
        }
    }

    public static class SecondHalf extends MocBattle {

        public SecondHalf(List<AbstractCharacter<?>> players) {
            super(new ScalegorgeTidalflowTubalance());
            this.setPlayerTeam(players);
        }
    }


    public static class ScalegorgeTidalflowTubalance extends MocTurbulence {
        // Too much headache for now
    }


}