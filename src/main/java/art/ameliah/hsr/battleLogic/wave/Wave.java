package art.ameliah.hsr.battleLogic.wave;

import art.ameliah.hsr.enemies.AbstractEnemy;

import java.util.List;

public interface Wave {

    /**
     * The enemies to spawn when the wave starts
     * @return list of enemies
     */
    List<AbstractEnemy> startEnemies();

    /**
     *
     * @return if the wave still has new enemies to spawn
     */
    boolean hasNext();

    /**
     * Next enemy to spawn on the field
     * @return the enemy to spawn in
     */
    AbstractEnemy nextEnemy();

    /**
     *
     * @return Max amount of enemies on the field
     */
    int maxEnemiesOnField();

    default String Identifier() {
        return this.getClass().getSimpleName();
    }

}
