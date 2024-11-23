package art.ameliah.hsr.battleLogic.wave.pf;

import art.ameliah.hsr.battleLogic.wave.Wave;
import art.ameliah.hsr.enemies.AbstractEnemy;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@RequiredArgsConstructor
public abstract class PfWave implements Wave {

    private final AbstractEnemy boss;
    private final Map<Class<? extends AbstractEnemy>, Supplier<AbstractEnemy>> minions = new HashMap<>();
    private final Map<Class<? extends AbstractEnemy>, Integer> usages = new HashMap<>();

    public void addMinionType(Class<? extends AbstractEnemy> clazz, int maxAmount, Supplier<AbstractEnemy> supplier) {
        this.minions.put(clazz, supplier);
        this.usages.put(clazz, maxAmount);
    }

    public boolean isBoss(AbstractEnemy enemy) {
        return this.boss == null || this.boss == enemy;
    }

    @Override
    public boolean hasNext() {
        if (this.boss != null) {
            return !this.boss.isDead();
        }

        for (var e : this.usages.entrySet()) {
            if (e.getValue() == 0) {
                return true;
            }
        }

        return false;
    }

    private boolean canSpawn(AbstractEnemy enemy) {
        return this.usages.get(enemy.getClass()) > 0;
    }

    @Override
    public AbstractEnemy nextEnemy(AbstractEnemy enemy) {
        if (this.canSpawn(enemy)) {
            this.usages.computeIfPresent(enemy.getClass(), (_, v) -> v - 1);
            return this.minions.get(enemy.getClass()).get();
        }

        var clazz = this.usages.entrySet()
                .stream()
                .filter(e -> e.getValue() > 0)
                .map(Map.Entry::getKey)
                .findFirst().orElse(null);

        if (clazz == null) {
            throw new IllegalStateException("Called next enemy, when no enemies were left");
        }

        this.usages.computeIfPresent(clazz, (_, v) -> v - 1);
        return this.minions.get(clazz).get();
    }

    @Override
    public int maxEnemiesOnField() {
        return 5;
    }
}
