package art.ameliah.hsr.battleLogic.log.lines.enemy;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;

public record ForcedAttack(AbstractEnemy enemy, AbstractCharacter<?> hit) implements Loggable {

    @Override
    public String asString() {
        return this.enemy.name + " forced to attack " + this.hit.name;
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
