package art.ameliah.hsr.battleLogic.wave.pf;

import art.ameliah.hsr.battleLogic.wave.Wave;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.registry.EnemyRegistry;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@RequiredArgsConstructor
public class PfWave implements Wave {

    private final Queue<AbstractEnemy> enemyQueue = new LinkedList<>();
    private AbstractEnemy boss;

    public boolean isBoss(AbstractEnemy enemy) {
        return this.boss != null && this.boss == enemy;
    }

    public PfWave addEnemies(int... ids) {
        for (int id : ids) {
            this.addEnemy(id);
        }
        return this;
    }

    public PfWave addEnemy(int id) {
        try {
            this.addEnemy(EnemyRegistry.getEnemy(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public PfWave addEnemy(AbstractEnemy enemy) {
        return this.addEnemy(enemy, false);
    }

    public PfWave addEnemy(AbstractEnemy enemy, boolean isBoss) {
        this.enemyQueue.offer(enemy);

        if (isBoss) {
            if (this.boss != null) {
                throw new IllegalStateException("PureFiction wave cannot have more than one boss");
            }
            this.boss = enemy;
        }

        return this;
    }

    @Override
    public List<AbstractEnemy> startEnemies() {
        List<AbstractEnemy> enemies = new ArrayList<>();

        while (enemies.size() < this.maxEnemiesOnField() && !this.enemyQueue.isEmpty()) {
            enemies.add(this.enemyQueue.poll());
        }

        return enemies;
    }

    @Override
    public boolean hasNext() {
        if (this.boss != null) {
            return !this.boss.isDead();
        }

        return !this.enemyQueue.isEmpty();
    }

    @Override
    public AbstractEnemy nextEnemy() {
        return this.enemyQueue.poll();
    }

    @Override
    public int maxEnemiesOnField() {
        return 5;
    }

    @Override
    public String toString() {
        return "PfWave{" +
                "enemyQueue=" + enemyQueue +
                ", boss=" + boss +
                '}';
    }
}
