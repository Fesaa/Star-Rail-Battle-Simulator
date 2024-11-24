package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.enemies.AbstractEnemy;

import java.util.List;
import java.util.stream.Collectors;

public record TurnEnd(AbstractEntity entity, List<AbstractEnemy> enemies) implements Loggable {
    @Override
    public String asString() {
        String s = this.enemies
                .stream()
                .map(e -> String.format("%s(%,.0f)", e.getName(), e.getCurrentHp()))
                .collect(Collectors.joining(","));

        return String.format("%s's turn has ended %s", this.entity.name, s);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
