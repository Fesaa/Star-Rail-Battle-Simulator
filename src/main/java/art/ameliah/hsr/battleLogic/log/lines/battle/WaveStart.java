package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.battleLogic.wave.Wave;
import art.ameliah.hsr.utils.Comparators;

import java.util.stream.Collectors;

public record WaveStart(Wave wave, IBattle battle) implements Loggable {

    @Override
    public String asString() {
        return String.format("Wave %s has started.\n%s", wave.Identifier(),battle.getActionValueMap().entrySet()
                .stream()
                .sorted(Comparators::CompareSpd)
                .map(e -> e.getKey().getName() + "=" + e.getValue())
                .collect(Collectors.joining(", ", "{", "}")));
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
