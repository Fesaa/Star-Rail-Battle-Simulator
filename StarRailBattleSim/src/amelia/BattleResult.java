package amelia;

import battleLogic.log.lines.metrics.BattleMetrics;
import battleLogic.log.lines.metrics.FinalDmgMetrics;
import battleLogic.log.lines.metrics.PostCombatPlayerMetrics;
import characters.AbstractCharacter;

import java.util.List;

public class BattleResult {

    public List<AbstractCharacter<?>> team;
    public FinalDmgMetrics finalDmgMetrics;
    public BattleMetrics battleMetrics;
    public PostCombatPlayerMetrics postCombatPlayerMetrics;

}
