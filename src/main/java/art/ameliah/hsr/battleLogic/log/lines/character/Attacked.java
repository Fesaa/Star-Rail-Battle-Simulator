package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public record Attacked(AbstractEntity source, AbstractEntity target, float damage) implements Loggable {


    @Override
    public String asString() {
        return source.name + " attacked "
                + String.format("%s (%.0f)", target.name, target.getCurrentHp())
                + " for " + damage + " damage";
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
