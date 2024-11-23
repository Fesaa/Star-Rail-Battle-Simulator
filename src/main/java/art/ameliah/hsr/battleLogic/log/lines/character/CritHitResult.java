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
        return String.format("%s hit %s(%.1f) for expected crit result of %.3f damage", this.source.name, this.target.name, this.target.getCurrentHp(), calculatedDamage);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
