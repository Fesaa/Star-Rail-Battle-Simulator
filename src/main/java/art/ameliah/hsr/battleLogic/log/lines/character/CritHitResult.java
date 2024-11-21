package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public record CritHitResult(AbstractCharacter<?> source, AbstractEnemy target, double calculatedDamage) implements Loggable {

    @Override
    public String asString() {
        return String.format("%s hit %s for expected crit result of %.0f damage", this.source.name, this.target.name, calculatedDamage);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
