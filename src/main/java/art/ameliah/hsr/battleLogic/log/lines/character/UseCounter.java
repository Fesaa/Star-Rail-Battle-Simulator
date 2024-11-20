package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;

public class UseCounter implements Loggable {

    public final AbstractCharacter<?> character;

    public UseCounter(AbstractCharacter<?> character) {
        this.character = character;
    }

    @Override
    public String asString() {
        return this.character.name + " used Counter";
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
