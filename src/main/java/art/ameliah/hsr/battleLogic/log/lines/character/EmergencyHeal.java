package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;

public class EmergencyHeal implements Loggable {

    public final AbstractCharacter<?> character;

    public EmergencyHeal(AbstractCharacter<?> character) {
        this.character = character;
    }

    @Override
    public String asString() {
        return "Triggering " + this.character.name + " Emergency Heal";
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
