package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;

import java.util.List;
import java.util.stream.Collectors;

public record TriggerTechnique(List<AbstractCharacter<?>> characters) implements Loggable {

    @Override
    public String asString() {
        return "Triggering Techniques for " + this.characters
                .stream()
                .map(c -> c.getName())
                .collect(Collectors.joining(", "));
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
