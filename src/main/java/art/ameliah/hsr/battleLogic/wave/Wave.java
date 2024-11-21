package art.ameliah.hsr.battleLogic.wave;

import art.ameliah.hsr.enemies.AbstractEnemy;

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
