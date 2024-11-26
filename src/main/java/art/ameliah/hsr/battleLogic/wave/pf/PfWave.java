package art.ameliah.hsr.battleLogic.wave.pf;

import art.ameliah.hsr.battleLogic.wave.Wave;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.registry.EnemyRegistry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@RequiredArgsConstructor
public class PfWave implements Wave {

    private final Queue<AbstractEnemy> enemyQueue = new LinkedList<>();
    @Getter
    private AbstractEnemy boss;
    private int totalEnemies = 0;

    public int totalMinions() {
        return this.hasBoss() ? this.totalEnemies - 1 : this.totalEnemies;
    }

    public boolean hasBoss() {
        return this.boss != null;
    }

    public boolean isBoss(AbstractEnemy enemy) {
        return this.boss != null && this.boss == enemy;
    }

    public void addEnemies(boolean hasBoss, int... ids) {
        for (int idx = 0; idx < ids.length; idx++) {
            this.addEnemy(ids[idx], idx == 2 && hasBoss);
        }
    }

    public void addEnemy(int id, boolean isBoss) {
        try {
            this.addEnemy(EnemyRegistry.getEnemy(id), isBoss);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

        this.totalEnemies++;
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
            return !this.boss.isDead() && !this.enemyQueue.isEmpty();
        }

        return !this.enemyQueue.isEmpty();
    }

    @Override
    public AbstractEnemy nextEnemy() {
        return this.enemyQueue.poll();
    }

    public int size() {
        return this.enemyQueue.size();
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
