package art.ameliah.hsr.battleLogic.wave;

import art.ameliah.hsr.battleLogic.Battle;
import art.ameliah.hsr.battleLogic.log.lines.battle.WaveEnd;
import art.ameliah.hsr.battleLogic.log.lines.battle.WaveStart;
import art.ameliah.hsr.enemies.AbstractEnemy;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

public abstract class WavedBattle extends Battle {

    protected final Queue<Wave> waves = new ArrayDeque<>();
    protected Wave currentWave;

    public WavedBattle(Wave ...waves) {
        Collections.addAll(this.waves, waves);
    }

    protected void addWave(Wave wave) {
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
        if (this.currentWave == null) {
            throw new ForceBattleEnd("No waves left");
        }
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
