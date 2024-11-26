package art.ameliah.hsr.battleLogic.log.lines.enemy;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;

public record GainedWeakness(AbstractEnemy enemy, ElementType weakness) implements Loggable {
    @Override
    public String asString() {
        return String.format("%s gained weakness: %s -> %s", this.enemy.getName(), this.weakness, this.enemy.getWeaknesses());
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
