package art.ameliah.hsr.battleLogic.wave.moc;

import art.ameliah.hsr.battleLogic.wave.Wave;
import art.ameliah.hsr.enemies.AbstractEnemy;

import java.util.Arrays;
import java.util.List;

public class MocWave implements Wave {

    private final List<AbstractEnemy> enemies;

    public MocWave(List<AbstractEnemy> enemies) {
        this.enemies = enemies;
    }

    public MocWave(AbstractEnemy... enemies) {
        this(Arrays.asList(enemies));
    }

    @Override
    public List<AbstractEnemy> startEnemies() {
        return this.enemies;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public AbstractEnemy nextEnemy() {
        return null;
    }

    @Override
    public int maxEnemiesOnField() {
        return 5;
    }
}
