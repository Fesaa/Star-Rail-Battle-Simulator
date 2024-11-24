package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public record TurnStart(AbstractEntity next, float atAV,
                        HashMap<AbstractEntity, Float> actionValueMap) implements Loggable {

    public TurnStart(AbstractEntity next, float atAV, HashMap<AbstractEntity, Float> actionValueMap) {
        this.next = next;
        this.atAV = atAV;
        this.actionValueMap = new HashMap<>(actionValueMap); // Copy the map to prevent modification
    }

    @Override
    public String asString() {
        return "Next is " + this.next.name + " at " + this.atAV + " action value " +
                actionValueMap.entrySet()
                        .stream()
                        .sorted((Map.Entry.comparingByValue()))
                        .map(e -> e.getKey().name + "=" + e.getValue())
                        .collect(Collectors.joining(", ", "{", "}"));
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
