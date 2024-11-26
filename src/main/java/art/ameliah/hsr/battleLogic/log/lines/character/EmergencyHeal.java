package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;

public record EmergencyHeal(AbstractCharacter<?> character) implements Loggable {

    @Override
    public String asString() {
        return "Triggering " + this.character.getName() + " Emergency Heal";
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
