package art.ameliah.hsr.battleLogic.log.lines.metrics;

import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;


public class BattleMetrics implements Loggable {

    public final IBattle battle;

    public BattleMetrics(IBattle battle) {
        this.battle = battle;
    }

    @Override
    public String asString() {
        return this.battle.metrics();
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
