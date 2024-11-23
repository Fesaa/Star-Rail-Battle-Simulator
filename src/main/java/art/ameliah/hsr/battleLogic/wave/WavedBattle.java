package art.ameliah.hsr.battleLogic.wave;

import art.ameliah.hsr.battleLogic.Battle;
import art.ameliah.hsr.battleLogic.log.lines.battle.WaveEnd;
import art.ameliah.hsr.battleLogic.log.lines.battle.WaveStart;
import art.ameliah.hsr.enemies.AbstractEnemy;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

public abstract class WavedBattle<T extends Wave> extends Battle {

    protected final Queue<T> waves = new ArrayDeque<>();
    protected T currentWave;

    @SafeVarargs
    public WavedBattle(T ...waves) {
        Collections.addAll(this.waves, waves);
    }

    protected void addWave(T wave) {
        this.waves.offer(wave);
    }

    @Override
    public void onStart() {
        if (this.waves.isEmpty()) {
            throw new IllegalStateException("No waves were added to the battle");
        }

        this.currentWave = this.waves.remove();
        this.setEnemyTeam(this.currentWave.startEnemies());
    }

    @Override
    protected void onEnemyRemove(AbstractEnemy enemy, int idx) {
        if (!this.currentWave.hasNext() && getEnemies().isEmpty()) {
            this.goToNextWave();
            return;
        }

        if (!this.currentWave.hasNext()) {
            return;
        }

        AbstractEnemy newEnemy = this.currentWave.nextEnemy(enemy);
        this.addEnemyAt(newEnemy, idx);
    }

    protected void goToNextWave() {
        addToLog(new WaveEnd(this.currentWave));
        this.currentWave = this.waves.poll();
        if (this.currentWave == null) {
            throw new ForceBattleEnd("No waves left");
        }
        addToLog(new WaveStart(this.currentWave));
        this.currentWave.startEnemies().forEach(this::addEnemy);
        this.onWaveChange();
    }

    protected abstract void onWaveChange();

}
