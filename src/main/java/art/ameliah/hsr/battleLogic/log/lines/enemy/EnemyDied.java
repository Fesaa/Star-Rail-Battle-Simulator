package art.ameliah.hsr.battleLogic.log.lines.enemy;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;

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
