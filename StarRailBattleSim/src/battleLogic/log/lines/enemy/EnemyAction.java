package battleLogic.log.lines.enemy;

import battleLogic.log.Loggable;
import battleLogic.log.Logger;
import characters.AbstractCharacter;
import enemies.AbstractEnemy;
import enemies.EnemyAttackType;

public class EnemyAction implements Loggable {

    public final AbstractEnemy enemy;
    public final AbstractCharacter<?> hit;
    public final EnemyAttackType attackType;
    public final String action;

    public EnemyAction(AbstractEnemy enemy, AbstractCharacter<?> hit, EnemyAttackType attackType) {
        this.enemy = enemy;
        this.hit = hit;
        this.attackType = attackType;
        this.action = null;
    }

    public EnemyAction(AbstractEnemy enemy, AbstractCharacter<?> hit, String action) {
        this.enemy = enemy;
        this.hit = hit;
        this.attackType = null;
        this.action = action;
    }

    public EnemyAction(AbstractEnemy enemy, String action) {
        this.enemy = enemy;
        this.hit = null;
        this.attackType = null;
        this.action = action;
    }

    @Override
    public String asString() {
        if (this.attackType == null) {
            return this.enemy.name + ": " + this.action;
        }

        if (this.attackType.equals(EnemyAttackType.AOE)) {
            return this.enemy.name + " used AOE attack";
        }

        return this.enemy.name + " uses " + this.attackType + " attack against " + this.hit.name;
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
