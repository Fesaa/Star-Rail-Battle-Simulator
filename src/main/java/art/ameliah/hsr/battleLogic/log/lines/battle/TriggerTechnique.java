package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;

import java.util.List;
import java.util.stream.Collectors;

public class TriggerTechnique implements Loggable {

    public final List<AbstractCharacter<?>> characters;

    public TriggerTechnique(List<AbstractCharacter<?>> characters) {
        this.characters = characters;
    }

    @Override
    public String asString() {
        return "Triggering Techniques for " + this.characters
                .stream()
                .map(c -> c.name)
                .collect(Collectors.joining(", "));
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
