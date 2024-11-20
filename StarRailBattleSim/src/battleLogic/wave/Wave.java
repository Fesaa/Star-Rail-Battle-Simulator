package battleLogic.wave;

import enemies.AbstractEnemy;

import java.util.List;

public interface Wave {

    List<AbstractEnemy> startEnemies();

    boolean hasNext();
    AbstractEnemy nextEnemy();

    int maxEnemiesOnField();

    default String Identifier() {
        return this.getClass().getSimpleName();
    }

}
