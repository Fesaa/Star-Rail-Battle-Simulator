package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;

public record HealthChange(AbstractCharacter<?> character, float amount, float full) implements Loggable {
    @Override
    public String asString() {
        return String.format("%s(%,.3f) health %s by %,.3f(%,.3f)", character.getName(),
                character.getCurrentHp().get(), amount < 0 ? "reduced" : "increased", amount, full);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
