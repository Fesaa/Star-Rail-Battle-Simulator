package art.ameliah.hsr.battleLogic.combat.ally;

import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.combat.base.AbstractDelayAttack;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DelayAttack extends AbstractDelayAttack<AbstractCharacter<?>, AbstractEnemy, AttackLogic> {

    public DelayAttack(AbstractCharacter<?> source) {
        super(source);
    }

    /**
     * Add logic for an enemy at position idx. Uses {@link IBattle#enemyCallback(int, Consumer)}
     * This will also add the enemy to the target set
     *
     * @param idx   the enemies index
     * @param logic the logic to execute when attacking
     */
    public void logic(int idx, BiConsumer<AbstractEnemy, AttackLogic> logic) {
        getBattle().enemyCallback(idx, e -> {
            this.addEnemy(e);
            this.logic(al -> logic.accept(e, al));
        });
    }
}
