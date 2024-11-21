package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;

public class UltDecision implements Loggable {

    private final AbstractCharacter<?> character;
    private final Class<?> ultGoal;

    public UltDecision(AbstractCharacter<?> character, Class<?> ultGoal) {
        this.character = character;
        this.ultGoal = ultGoal;
    }

    @Override
    public String asString() {
        return character.name + " decided to ULT because " + ultGoal.getSimpleName();
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
