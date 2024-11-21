package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;

public record ExtraHits(AbstractCharacter<?> character, int numExtraHits) implements Loggable {

    @Override
    public String asString() {
        return String.format("%s rolled %,d extra hits", character.name, numExtraHits);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
