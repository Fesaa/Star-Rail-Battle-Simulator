package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;
import lombok.Getter;

@Getter
public final class HealthChange implements Loggable {
    private final AbstractCharacter<?> character;
    private final float amount;
    private final float full;

    public HealthChange(AbstractCharacter<?> character, float amount, float full) {
        this.character = character;
        this.amount = amount == -0 ? 0 : amount;
        this.full = full == -0 ? 0 : full;
    }

    @Override
    public String asString() {
        return String.format("%s(%,.3f -> %,.3f) health %s by %,.3f(%,.3f) [%,.3f%%]", character.getName(),
                character.getCurrentHp().get() - amount, character.getCurrentHp().get(),
                amount < 0 ? "reduced" : "increased", amount, full, character.getCurrentHp().get()/character.getFinalHP()*100);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
