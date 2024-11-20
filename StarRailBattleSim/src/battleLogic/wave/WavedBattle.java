package battleLogic.wave;

import battleLogic.Battle;
import battleLogic.log.lines.battle.WaveEnd;
import battleLogic.log.lines.battle.WaveStart;
import enemies.AbstractEnemy;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

public abstract class WavedBattle extends Battle {

    protected final Queue<Wave> waves = new ArrayDeque<>();
    private Wave currentWave;

    public WavedBattle(Wave ...waves) {
        Collections.addAll(this.waves, waves);
        this.currentWave = this.waves.remove();
    }

    protected void addWave(Wave wave) {
        this.waves.offer(wave);
    }

    @Override
    protected void onEnemyRemove() {
        if (!this.currentWave.hasNext() && getEnemies().isEmpty()) {
            this.goToNextWave();
            return;
        }

        this.fillField();
    }

    protected void goToNextWave() {
        addToLog(new WaveEnd(this.currentWave));
        this.currentWave = this.waves.poll();
        addToLog(new WaveStart(this.currentWave));
        this.fillField();
        this.onWaveChange();
    }

    protected void fillField() {
        while (getEnemies().size() < this.currentWave.maxEnemiesOnField()) {
            AbstractEnemy nextEnemy = currentWave.nextEnemy();
            if (nextEnemy == null) {
                break;
            }

            this.addEnemy(nextEnemy);
        }
    }

    protected abstract void onWaveChange();

}
