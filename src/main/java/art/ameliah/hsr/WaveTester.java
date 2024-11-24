package art.ameliah.hsr;

import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.log.DefaultLogger;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.lines.battle.AdvanceEntity;
import art.ameliah.hsr.battleLogic.log.lines.battle.DelayEntity;
import art.ameliah.hsr.battleLogic.log.lines.battle.TurnEnd;
import art.ameliah.hsr.battleLogic.log.lines.battle.TurnStart;
import art.ameliah.hsr.battleLogic.log.lines.battle.WaveEnd;
import art.ameliah.hsr.battleLogic.log.lines.battle.WaveStart;
import art.ameliah.hsr.battleLogic.log.lines.character.Attacked;
import art.ameliah.hsr.battleLogic.log.lines.character.TurnDecision;
import art.ameliah.hsr.battleLogic.log.lines.character.UltDecision;
import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyAction;
import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyDied;
import art.ameliah.hsr.battleLogic.log.lines.entity.GainPower;
import art.ameliah.hsr.battleLogic.log.lines.metrics.BattleMetrics;
import art.ameliah.hsr.battleLogic.log.lines.metrics.FinalDmgMetrics;
import art.ameliah.hsr.battleLogic.log.lines.metrics.PostCombatPlayerMetrics;
import art.ameliah.hsr.battleLogic.log.lines.metrics.PreCombatPlayerMetrics;
import art.ameliah.hsr.battleLogic.wave.moc.Moc;
import art.ameliah.hsr.battleLogic.wave.pf.PureFiction;
import art.ameliah.hsr.game.moc.ScalegorgeTidalflow11;
import art.ameliah.hsr.game.pf.technicalityentrapment.TechnicalityEntrapment;
import art.ameliah.hsr.teams.Divteams;
import art.ameliah.hsr.teams.FeixiaoTeams;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import static art.ameliah.hsr.teams.FeixiaoTeams.FeixiaoMarch;


public class WaveTester {

    public static void MocTest() {
        Moc moc = new ScalegorgeTidalflow11(
                FeixiaoMarch(FeixiaoTeams::myRobin, FeixiaoTeams::myHuoHuo),
                FeixiaoMarch(FeixiaoTeams::myRobin, FeixiaoTeams::myHuoHuo));
        moc.setBattleLogger(WaveTesterLogger::new);
        moc.Start();
    }

    public static void MocDivTest() {
        Moc moc = new ScalegorgeTidalflow11(Divteams.players(), List.of());
        moc.setBattleLogger(WaveTesterLogger::new);
        moc.Start();
    }

    public static void PfTest() {
        PureFiction pureFiction = new TechnicalityEntrapment(Divteams.players(), Divteams.players());
        pureFiction.setBattleLogger(WaveTesterLogger::new);
        pureFiction.Start();
    }

    public static class WaveTesterLogger extends DefaultLogger {

        public WaveTesterLogger(IBattle battle) {
            super(battle);

            PrintStream printStream;
            try {
                printStream = new PrintStream(new FileOutputStream("export_2/" + battle.getClass().getSimpleName() + ".log"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            this.out = printStream;

        }

        @Override
        public void handle(FinalDmgMetrics finalDmgMetrics) {
            System.out.println(finalDmgMetrics.asString());
            this.log(finalDmgMetrics);
        }

        @Override
        public void handle(TurnStart turnStart) {
            this.out.println();
            this.log(turnStart);
        }

        @Override
        public void handle(TurnEnd turnEnd) {
            this.log(turnEnd);
            this.out.println();
        }

    }

}
