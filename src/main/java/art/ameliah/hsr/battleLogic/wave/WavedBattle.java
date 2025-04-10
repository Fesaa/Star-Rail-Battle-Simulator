package art.ameliah.hsr.battleLogic.wave;

import art.ameliah.hsr.battleLogic.Battle;
import art.ameliah.hsr.battleLogic.log.lines.battle.WaveEnd;
import art.ameliah.hsr.battleLogic.log.lines.battle.WaveStart;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.combat.WaveStartEvent;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

public abstract class WavedBattle<T extends Wave> extends Battle {

    protected final Queue<T> waves = new ArrayDeque<>();
    private final Queue<Integer> talliedPositions = new ArrayDeque<>();
    protected T currentWave;

    @SafeVarargs
    public WavedBattle(T... waves) {
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
        this.getPlayers().forEach(p -> p.getEventBus().fire(new WaveStartEvent()));
    }

    @Override
    protected void onEnemyRemove(AbstractEnemy enemy, int idx) {
        if (!this.currentWave.hasNext() && getEnemies().isEmpty()) {
            this.goToNextWave();
            return;
        }

        this.talliedPositions.offer(idx);

        if (getEnemies().isEmpty()) {
            this.fillField();
        }
    }

    @Override
    protected void onEndTurn() {
        this.fillField();
    }

    @Override
    protected void onTurnStart() {
        this.fillField();
    }

    @Override
    protected void tryUlts() {
        super.tryUlts();
        this.fillField();
    }

    @Override
    public int maxEnemiesOnField() {
        return this.currentWave.maxEnemiesOnField();
    }

    protected final void fillField() {
        while (this.enemyTeam.size() < this.currentWave.maxEnemiesOnField() && this.currentWave.hasNext()) {
            AbstractEnemy nextEnemy = this.currentWave.nextEnemy();
            Integer nextIdx = this.talliedPositions.poll();
            if (nextIdx == null) {
                throw new IllegalStateException("Null index saved");
            }

            this.addEnemyAt(nextEnemy, nextIdx);
            this.afterEnemyAdd(nextEnemy, nextIdx);
        }
        this.talliedPositions.clear();
    }

    protected void goToNextWave() {
        addToLog(new WaveEnd(this.currentWave));
        this.currentWave = this.waves.poll();
        if (this.currentWave == null) {
            throw new ForceBattleEnd("No waves left");
        }
        this.talliedPositions.clear();
        this.currentWave.startEnemies().forEach(this::addEnemy);
        this.onWaveChange();

        addToLog(new WaveStart(this.currentWave, this));
        this.getPlayers().forEach(p -> p.getEventBus().fire(new WaveStartEvent()));
        getPlayers().forEach(AbstractCharacter::tryUltimate);
    }

    protected abstract void onWaveChange();

    protected abstract void afterEnemyAdd(AbstractEnemy enemy, int idx);

}
