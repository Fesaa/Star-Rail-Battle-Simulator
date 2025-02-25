package art.ameliah.hsr.game.moc;


import art.ameliah.hsr.battleLogic.wave.moc.Moc;
import art.ameliah.hsr.battleLogic.wave.moc.MocBattle;
import art.ameliah.hsr.battleLogic.wave.moc.MocTurbulence;
import art.ameliah.hsr.battleLogic.wave.moc.MocWave;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.game.jarilovi.fragmentum.EverwinterShadewalker;
import art.ameliah.hsr.enemies.game.jarilovi.fragmentum.FrigidProwler;
import art.ameliah.hsr.enemies.game.jarilovi.fragmentum.GuardianShadow;
import art.ameliah.hsr.enemies.game.jarilovi.fragmentum.IceOutOfSpace;
import art.ameliah.hsr.enemies.game.luofu.AurumatonSpectralEnvoy;
import art.ameliah.hsr.enemies.game.stellaronhunters.Kafka;

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
            this.addWave(new MocWave(new GuardianShadow(), new Kafka(1084176)));
        }
    }

    public static class SecondHalf extends MocBattle {

        public SecondHalf(List<AbstractCharacter<?>> players) {
            super(new ScalegorgeTidalflowTubalance());
            this.setPlayerTeam(players);
            this.addWave(new MocWave(new IceOutOfSpace(), new FrigidProwler(), new EverwinterShadewalker()));
        }
    }


    public static class ScalegorgeTidalflowTubalance extends MocTurbulence {

        public ScalegorgeTidalflowTubalance() {
            super("Scalegorge Tidalflow Tubalance");
        }

        @Override
        protected void trigger() {

        }
    }


}
