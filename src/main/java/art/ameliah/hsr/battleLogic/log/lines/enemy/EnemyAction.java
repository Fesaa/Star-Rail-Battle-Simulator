package art.ameliah.hsr.battleLogic.log.lines.enemy;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyAttackType;

public class EnemyAction implements Loggable {

    public final AbstractEnemy enemy;
    public final AbstractCharacter<?> hit;
    public final EnemyAttackType attackType;
    public final String action;

    public EnemyAction(AbstractEnemy enemy, AbstractCharacter<?> hit, EnemyAttackType attackType, String action) {
        this.enemy = enemy;
        this.hit = hit;
        this.attackType = attackType;
        this.action = action;
    }

    public EnemyAction(AbstractEnemy enemy, AbstractCharacter<?> hit, EnemyAttackType attackType) {
        this(enemy, hit, attackType, null);
    }

    public EnemyAction(AbstractEnemy enemy, EnemyAttackType attackType, String action) {
        this(enemy, null, attackType, action);
    }

    public EnemyAction(AbstractEnemy enemy, AbstractCharacter<?> hit, String action) {
        this(enemy, hit, null, action);
    }

    public EnemyAction(AbstractEnemy enemy, String action) {
        this(enemy, null, null, action);
    }

    @Override
    public String asString() {
        StringBuilder sb = new StringBuilder();

        if (this.enemy != null) {
            sb.append(this.enemy.name);
        } else {
            sb.append("unknown");
        }

        sb.append(" uses ");

        if (this.attackType != null) {
            if (this.attackType.equals(EnemyAttackType.AOE)) {
                sb.append(" AOE attack ");
            }
            sb.append(this.attackType);
        }

        if (this.action != null) {
            sb.append("(").append(this.action).append(")");
        }

        if (this.hit != null) {
            sb.append(" against ").append(this.hit.name);
        }

        return sb.toString();
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
