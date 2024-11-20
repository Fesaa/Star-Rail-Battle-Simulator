package battleLogic.log.lines.enemy;

import battleLogic.log.Loggable;
import battleLogic.log.Logger;
import characters.AbstractCharacter;
import enemies.AbstractEnemy;

public class EnemyDied implements Loggable {

    public final AbstractEnemy enemy;
    public final AbstractCharacter<?> reason;

    public EnemyDied(AbstractEnemy enemy, AbstractCharacter<?> reason) {
        this.enemy = enemy;
        this.reason = reason;
    }

    @Override
    public String asString() {
        return String.format("Enemy %s died after %s attacked them", enemy.name, reason.name);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
