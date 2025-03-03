package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.utils.Comparators;

import java.util.HashMap;
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
        return "Next is " + this.next.getName() + " at " + this.atAV + " action value " +
                actionValueMap.entrySet()
                        .stream()
                        .sorted(Comparators::CompareSpd)
                        .map(e -> e.getKey().getName() + "=" + e.getValue())
                        .collect(Collectors.joining(", ", "{", "}"));
    }

    @Override
    public String prefix() {
        return "\n";
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
