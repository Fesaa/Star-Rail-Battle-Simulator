package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;

public record UseCounter(AbstractCharacter<?> character) implements Loggable {

    @Override
    public String asString() {
        return this.character.getName() + " used Counter";
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
