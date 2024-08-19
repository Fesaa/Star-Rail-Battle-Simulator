package battleLogic.log.lines.character;

import battleLogic.log.LogLine;
import battleLogic.log.Logger;
import characters.AbstractCharacter;

public class ExtraHits extends LogLine {

    private final AbstractCharacter character;
    private final int numExtraHits;

    public ExtraHits(AbstractCharacter character, int numExtraHits) {
        this.character = character;
        this.numExtraHits = numExtraHits;
    }

    @Override
    public String asString() {
        return String.format("%s rolled %d extra hits", character.name, numExtraHits);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
