package art.ameliah.hsr.battleLogic.wave.pf;

import art.ameliah.hsr.battleLogic.wave.Wave;
import art.ameliah.hsr.enemies.AbstractEnemy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PFWave implements Wave {

    private final Queue<AbstractEnemy> enemies;
    private final int maxOnField;

    public PFWave(Queue<AbstractEnemy> enemies, int maxOnField) {
        this.enemies = enemies;
        this.maxOnField = maxOnField;
    }

    public PFWave(int maxOnField, AbstractEnemy ...enemies) {
        this.enemies = new LinkedList<>();
        Collections.addAll(this.enemies, enemies);

        this.maxOnField = maxOnField;
    }

    @Override
    public List<AbstractEnemy> startEnemies() {
        List<AbstractEnemy> startEnemies = new ArrayList<>();
        while (!enemies.isEmpty() && startEnemies.size() < maxOnField) {
            startEnemies.add(enemies.poll());
        }
        return startEnemies;
    }

    @Override
    public boolean hasNext() {
        return !this.enemies.isEmpty();
    }

    @Override
    public AbstractEnemy nextEnemy() {
        return this.enemies.poll();
    }

    @Override
    public int maxEnemiesOnField() {
        return this.maxOnField;
    }
}
