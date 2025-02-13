package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;

public record LoseEnergy(AbstractCharacter<?> character, float initialEnergy, float currentEnergy, float energyLost,
                         String source) implements Loggable {
    @Override
    public String asString() {
        float actual = this.currentEnergy - this.initialEnergy;
        return String.format("%s lost %.3f(%.3f) Energy (%.3f -> %.3f) %s", this.character.getName(), energyLost, actual, initialEnergy, currentEnergy, source);

    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
