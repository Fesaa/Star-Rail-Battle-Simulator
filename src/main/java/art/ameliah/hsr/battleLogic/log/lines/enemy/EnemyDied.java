package art.ameliah.hsr.battleLogic.log.lines.enemy;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;

public class EnemyDied implements Loggable {

    private final AbstractEnemy enemy;
    private final String reason;

    public EnemyDied(AbstractEnemy enemy, AbstractCharacter<?> killer) {
        this(enemy, String.format("after %s attacked them", killer.name));
    }

    public EnemyDied(AbstractEnemy enemy, String reason) {
        this.enemy = enemy;
        this.reason = reason;
    }

    @Override
    public String asString() {
        return String.format("Enemy %s died %s", enemy, reason);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
