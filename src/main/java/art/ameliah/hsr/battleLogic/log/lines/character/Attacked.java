package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.DamageType;

import java.util.List;
import java.util.stream.Collectors;

public record Attacked(AbstractEntity source, AbstractEntity target, float damage,
                       List<DamageType> types) implements Loggable {


    @Override
    public String asString() {
        String s = source.name + " attacked "
                + String.format("%s (%.0f)", target.name, target.getCurrentHp())
                + " for " + damage + " damage";

        if (this.types.isEmpty()) {
            return s;
        }

        return s + " with " + types.stream().map(Enum::toString).collect(Collectors.joining(","));
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
