package art.ameliah.hsr.battleLogic.log.lines.enemy;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;

public class ForcedAttack implements Loggable {

    public final AbstractEnemy enemy;
    public final AbstractCharacter<?> hit;

    public ForcedAttack(AbstractEnemy enemy, AbstractCharacter<?> hit) {
        this.enemy = enemy;
        this.hit = hit;
    }

    @Override
    public String asString() {
        return this.enemy.name + " forced to attack " + this.hit.name;
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
