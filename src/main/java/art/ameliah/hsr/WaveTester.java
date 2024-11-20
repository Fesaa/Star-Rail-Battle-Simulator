package art.ameliah.hsr;

import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.log.DefaultLogger;
import art.ameliah.hsr.battleLogic.wave.moc.Moc;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.game.moc.ScalegorgeTidalflow11;

import java.util.ArrayList;
import java.util.List;

import static art.ameliah.hsr.teams.TopazTeams.*;


public class WaveTester {

    public static void MocTest() {
        List<AbstractCharacter<?>> players = new ArrayList<>();
        players.add(getPrebuiltFeixiaoTT());
        players.add(getPrebuiltSwordMarchTT());
        players.add(getPrebuiltRobinTT());
        players.add(getPrebuiltHuohuoTT());

        Moc moc = new ScalegorgeTidalflow11(players, new ArrayList<>());
        moc.setBattleLogger(WaveTesterLogger::new);
        moc.Start();
    }

    public static class WaveTesterLogger extends DefaultLogger {

        public WaveTesterLogger(IBattle battle) {
            super(battle);
        }
    }

}