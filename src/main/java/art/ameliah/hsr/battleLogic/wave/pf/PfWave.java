package art.ameliah.hsr.battleLogic.wave.pf;

import art.ameliah.hsr.battleLogic.wave.Wave;
import art.ameliah.hsr.enemies.AbstractEnemy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

public class PfWave implements Wave {

    private final Queue<AbstractEnemy> bosses = new LinkedList<>();
    private final List<Supplier<AbstractEnemy>> minionCycle = new ArrayList<>();

    private AbstractEnemy currentBoss = null;
    private int minionIdx = 0;

    public PfWave() {}

    public void addBoss(AbstractEnemy boss) {
        this.bosses.add(boss);
    }

    public void addMinionType(Supplier<AbstractEnemy> supplier) {
        this.minionCycle.add(supplier);
    }

    private AbstractEnemy getNextMinion() {
        AbstractEnemy next = this.minionCycle.get(minionIdx).get();
        minionIdx = (minionIdx + 1) % this.minionCycle.size();
        return next;
    }

    private AbstractEnemy getNextBoss() {
        if (bosses.isEmpty()) {
            throw new IllegalStateException("Wave has no bosses set up");
        }

        this.currentBoss = this.bosses.poll();
        return this.currentBoss;
    }

    public boolean isCurrentBoss(AbstractEnemy enemy) {
        return this.currentBoss == enemy;
    }

    @Override
    public List<AbstractEnemy> startEnemies() {
        return List.of(
                this.getNextMinion(),
                this.getNextMinion(),
                this.getNextBoss(),
                this.getNextMinion(),
                this.getNextMinion()
        );
    }

    @Override
    public boolean hasNext() {
        return !this.bosses.isEmpty();
    }

    @Override
    public AbstractEnemy nextEnemy(AbstractEnemy enemy) {
        if (!this.isCurrentBoss(enemy)) {
            return this.getNextMinion();
        }
        return this.getNextBoss();
    }

    @Override
    public int maxEnemiesOnField() {
        return 5;
    }
}
