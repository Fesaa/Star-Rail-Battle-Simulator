package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.DamageType;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record Attacked(AbstractEntity source, Collection<AbstractEntity> targets, float damage,
                       Collection<DamageType> types) implements Loggable {


    public Attacked(AbstractEntity source, AbstractEntity target, float damage,
                    Collection<DamageType> types) {
        this(source, Set.of(target), damage, types);
    }

    @Override
    public String asString() {
        String s = source.name + " attacked "
                + targets.stream().map(Objects::toString).collect(Collectors.joining(", "))
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
