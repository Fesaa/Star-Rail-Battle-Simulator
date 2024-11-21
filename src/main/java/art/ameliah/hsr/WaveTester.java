package art.ameliah.hsr;

import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.log.DefaultLogger;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.lines.battle.AdvanceEntity;
import art.ameliah.hsr.battleLogic.log.lines.battle.WaveEnd;
import art.ameliah.hsr.battleLogic.log.lines.battle.WaveStart;
import art.ameliah.hsr.battleLogic.log.lines.character.TurnDecision;
import art.ameliah.hsr.battleLogic.log.lines.character.UltDecision;
import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyAction;
import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyDied;
import art.ameliah.hsr.battleLogic.log.lines.metrics.BattleMetrics;
import art.ameliah.hsr.battleLogic.log.lines.metrics.FinalDmgMetrics;
import art.ameliah.hsr.battleLogic.log.lines.metrics.PostCombatPlayerMetrics;
import art.ameliah.hsr.battleLogic.log.lines.metrics.PreCombatPlayerMetrics;
import art.ameliah.hsr.battleLogic.wave.moc.Moc;
import art.ameliah.hsr.characters.huohuo.Huohuo;
import art.ameliah.hsr.characters.robin.Robin;
import art.ameliah.hsr.game.moc.ScalegorgeTidalflow11;
import art.ameliah.hsr.teams.FeixiaoTeams;

import static art.ameliah.hsr.teams.FeixiaoTeams.FeixiaoMarch;
import static art.ameliah.hsr.teams.FeixiaoTeams.*;


public class WaveTester {

    public static void MocTest() {
        Moc moc = new ScalegorgeTidalflow11(
                FeixiaoMarch(FeixiaoTeams::myRobin, FeixiaoTeams::myHuoHuo),
                FeixiaoMarch(FeixiaoTeams::myRobin, FeixiaoTeams::myHuoHuo));
        moc.setBattleLogger(WaveTesterLogger::new);
        moc.Start();
    }

    public static class WaveTesterLogger extends DefaultLogger {

        public WaveTesterLogger(IBattle battle) {
            super(battle);
        }

        @Override
        public void handle(PreCombatPlayerMetrics preCombatPlayerMetrics) {
            super.log(preCombatPlayerMetrics);
        }

        @Override
        public void handle(PostCombatPlayerMetrics postCombatPlayerMetrics) {
            super.log(postCombatPlayerMetrics);
        }

        @Override
        public void handle(FinalDmgMetrics finalDmgMetrics) {
            super.log(finalDmgMetrics);
        }

        @Override
        protected void log(Loggable loggable) {
        }

        @Override
        public void handle(BattleMetrics battleMetrics) {
            super.log(battleMetrics);
        }

        @Override
        public void handle(UltDecision ultDecision) {
            super.log(ultDecision);
        }

        @Override
        public void handle(TurnDecision turnDecision) {
            super.log(turnDecision);
        }

        @Override
        public void handle(EnemyDied enemyDied) {
            super.log(enemyDied);
        }

        @Override
        public void handle(EnemyAction enemyAction) {
            super.log(enemyAction);
        }

        @Override
        public void handle(AdvanceEntity advanceEntity) {
            super.log(advanceEntity);
        }

        @Override
        public void handle(WaveStart waveStart) {
            super.log(waveStart);
        }

        @Override
        public void handle(WaveEnd waveEnd) {
            super.log(waveEnd);
        }
    }

}
