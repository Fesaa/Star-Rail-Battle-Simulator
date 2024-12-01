package art.ameliah.hsr;

import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.log.DefaultLogger;
import art.ameliah.hsr.battleLogic.log.lines.battle.TurnEnd;
import art.ameliah.hsr.battleLogic.log.lines.battle.TurnStart;
import art.ameliah.hsr.battleLogic.log.lines.character.DoMove;
import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyAction;
import art.ameliah.hsr.battleLogic.log.lines.metrics.BattleMetrics;
import art.ameliah.hsr.battleLogic.wave.moc.Moc;
import art.ameliah.hsr.battleLogic.wave.pf.PureFiction;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.game.moc.ScalegorgeTidalflow11;
import art.ameliah.hsr.game.pf.technicalityentrapment.TechnicalityEntrapment;
import art.ameliah.hsr.teams.Divteams;
import art.ameliah.hsr.teams.FeixiaoTeams;
import art.ameliah.hsr.teams.PlayerTeam;

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

        AbstractCharacter<?> herta = PlayerTeam.getPreBuiltHerta();
        herta.isDPS = true;
        AbstractCharacter<?> herta2 = PlayerTeam.getPreBuiltHerta();
        herta2.isDPS = true;
        PureFiction pureFiction = new TechnicalityEntrapment(
                List.of(PlayerTeam.getPreBuiltJade(), herta, PlayerTeam.getPreBuiltRobin(), Divteams.divsHuoHuo()),
                List.of(PlayerTeam.getPreBuiltJade(), herta2, PlayerTeam.getPreBuiltRobin(), Divteams.divsHuoHuo()));
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
        public void handle(EnemyAction enemyAction) {
            System.out.println(this.getBattle().prefix() + enemyAction.asString());
            this.log(enemyAction);
        }

        @Override
        public void handle(DoMove doMove) {
            System.out.println(this.getBattle().prefix() + doMove.asString());
            this.log(doMove);
        }

        @Override
        public void handle(BattleMetrics battleMetrics) {
            System.out.println(battleMetrics.asString());
            this.log(battleMetrics);
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
