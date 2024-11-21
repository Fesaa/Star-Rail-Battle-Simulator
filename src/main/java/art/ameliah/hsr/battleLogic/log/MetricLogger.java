package art.ameliah.hsr.battleLogic.log;

import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.log.lines.metrics.BattleMetrics;
import art.ameliah.hsr.battleLogic.log.lines.metrics.EnemyMetrics;
import art.ameliah.hsr.battleLogic.log.lines.metrics.FinalDmgMetrics;
import art.ameliah.hsr.battleLogic.log.lines.metrics.PostCombatPlayerMetrics;
import art.ameliah.hsr.battleLogic.log.lines.metrics.PreCombatPlayerMetrics;

import java.io.PrintStream;

public class MetricLogger extends JsonLogger {

    public MetricLogger(IBattle battle, PrintStream out) {
        super(battle, out);
    }

    public MetricLogger(IBattle battle) {
        super(battle);
    }

    @Override
    protected void log(Loggable loggable) {
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
    public void handle(BattleMetrics battleMetrics) {
        super.log(battleMetrics);
    }

    @Override
    public void handle(EnemyMetrics enemyMetrics) {
        super.log(enemyMetrics);
    }

    @Override
    public void handle(FinalDmgMetrics finalDmgMetrics) {
        super.log(finalDmgMetrics);
    }
}
