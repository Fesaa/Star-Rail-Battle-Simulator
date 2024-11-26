package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;

public record GainEnergy(AbstractCharacter<?> character, float initialEnergy, float currentEnergy, float energyGained,
                         String source) implements Loggable {


    @Override
    public String asString() {
        return String.format("%s gained %.3f Energy (%.3f -> %.3f) %s", this.character.getName(), energyGained, initialEnergy, currentEnergy, source);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
