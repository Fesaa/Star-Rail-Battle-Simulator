package art.ameliah.hsr.battleLogic.log.lines.metrics;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


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
