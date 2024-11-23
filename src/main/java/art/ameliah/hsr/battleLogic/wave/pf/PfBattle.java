package art.ameliah.hsr.battleLogic.wave.pf;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.lines.battle.pf.SurgingGritState;
import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyDied;
import art.ameliah.hsr.battleLogic.wave.WavedBattle;
import art.ameliah.hsr.enemies.AbstractEnemy;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PfBattle extends WavedBattle<PfWave> {

    private final ConcordantTrucePower concordantTruce;
    private final PureFictionBuff pfBuff;
    private final ISurgingGrit surgingGrit;

    private boolean surgingGridActive = false;
    private int gridAmount;
    private int gridOverflow;

    private void increaseGridAmount(int amount) {
        if (this.surgingGridActive) {
            this.gridOverflow = Math.min(this.gridAmount + amount, 30);
            return;
        }

        this.gridAmount = Math.min(this.gridAmount + amount, 100);

        if (this.gridAmount == 100) {
            this.activeSurgingGrid();
        }
    }

    private void activeSurgingGrid() {
        this.gridAmount = 0;
        this.surgingGridActive = true;
        this.pfBuff.applySurgingGritBuff(this);
        this.surgingGrit.apply(this);

        SurgingGritEntity entity = new SurgingGritEntity(this);
        this.actionValueMap.put(entity, entity.getBaseAV());
        entity.setBattle(this);
        addToLog(new SurgingGritState(SurgingGritState.State.START));
    }

    private void endSurgingGrid() {
        this.surgingGridActive = false;
        this.increaseGridAmount(this.gridOverflow);
        this.gridOverflow = 0;

        this.pfBuff.removeSurgingGritBuff(this);
        this.surgingGrit.remove(this);
        addToLog(new SurgingGritState(SurgingGritState.State.END));
    }

    @Override
    public void onStart() {
        super.onStart();

        for (var player : getPlayers()) {
            player.addPower(this.concordantTruce);
        }
        this.pfBuff.applyGritMechanic(this);
    }

    @Override
    protected void onEnemyRemove(AbstractEnemy enemy, int idx) {
        this.increaseGridAmount(5);

        // If the boss dies, the wave ends
        if (this.currentWave.isBoss(enemy)) {
            this.enemyTeam.forEach(e -> {
                this.actionValueMap.remove(e);
                this.addToLog(new EnemyDied(e, "Pure Fiction boss died, all minions die as well"));
            });
            this.enemyTeam.clear();
            this.goToNextWave();
            return;
        }

        super.onEnemyRemove(enemy, idx);
    }

    @Override
    protected void addWave(PfWave wave) {
        if (this.waves.size() == 3) {
            throw new IllegalStateException("Pure Fiction already has three waves");
        }

        super.addWave(wave);
    }

    @Override
    protected void onWaveChange() {
    }

    @RequiredArgsConstructor
    public static class SurgingGritEntity extends AbstractEntity {

        private final PfBattle pf;

        @Override
        public void takeTurn() {
            getBattle().getActionValueMap().remove(this);
            this.pf.endSurgingGrid();
        }

        @Override
        public float getBaseAV() {
            return 100;
        }
    }
}
