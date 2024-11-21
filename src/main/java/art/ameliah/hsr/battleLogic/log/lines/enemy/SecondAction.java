package art.ameliah.hsr.battleLogic.log.lines.enemy;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.enemies.AbstractEnemy;

public record SecondAction(AbstractEnemy enemy) implements Loggable {

    @Override
    public String asString() {
        return this.enemy.name + " takes a second action";
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
