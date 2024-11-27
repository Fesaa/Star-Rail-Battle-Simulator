package art.ameliah.hsr.battleLogic.combat.enemy;

import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.combat.base.AbstractDelayAttack;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EnemyDelayAttack extends AbstractDelayAttack<AbstractEnemy, AbstractCharacter<?>, EnemyAttackLogic> {
    public EnemyDelayAttack(AbstractEnemy source) {
        super(source);
    }

    /**
     * Add logic for a character at position idx. Uses {@link IBattle#characterCallback(int, Consumer)}
     * This will also add the enemy to the target set
     *
     * @param idx   the characters index
     * @param logic the logic to execute when attacking
     */
    public void logic(int idx, BiConsumer<AbstractCharacter<?>, EnemyAttackLogic> logic) {
        getBattle().characterCallback(idx, e -> {
            this.addEnemy(e);
            this.logic(al -> logic.accept(e, al));
        });
    }
}
