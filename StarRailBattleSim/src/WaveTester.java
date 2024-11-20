import battleLogic.IBattle;
import battleLogic.log.DefaultLogger;
import battleLogic.log.Loggable;
import battleLogic.log.Logger;
import battleLogic.log.lines.character.BreakDamageHitResult;
import battleLogic.log.lines.character.CritHitResult;
import battleLogic.log.lines.character.GainEnergy;
import battleLogic.log.lines.entity.GainCharge;
import battleLogic.log.lines.entity.GainPower;
import battleLogic.wave.moc.Moc;
import characters.AbstractCharacter;
import game.moc.ScalegorgeTidalflow11;

import java.util.ArrayList;
import java.util.List;

import static teams.TopazTeams.*;

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
