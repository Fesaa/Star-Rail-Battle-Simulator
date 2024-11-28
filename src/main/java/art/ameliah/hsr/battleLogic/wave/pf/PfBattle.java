package art.ameliah.hsr.battleLogic.wave.pf;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.combat.hit.FixedHit;
import art.ameliah.hsr.battleLogic.log.lines.battle.pf.GainGridPoints;
import art.ameliah.hsr.battleLogic.log.lines.battle.pf.SurgingGritState;
import art.ameliah.hsr.battleLogic.log.lines.character.HitResultLine;
import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyDied;
import art.ameliah.hsr.battleLogic.wave.WavedBattle;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;
import org.jetbrains.annotations.Nullable;

public class PfBattle extends WavedBattle<PfWave> {

    private final ConcordantTrucePower concordantTruce;
    private final PureFictionBuff pfBuff;
    private final ISurgingGrit surgingGrit;
    private final SurgingGritEntity entity;

    private boolean surgingGridActive = false;
    private int gridAmount;
    private int gridOverflow;

    public PfBattle(ConcordantTrucePower concordantTruce, PureFictionBuff pfBuff, ISurgingGrit surgingGrit) {
        this.concordantTruce = concordantTruce;
        this.pfBuff = pfBuff;
        this.surgingGrit = surgingGrit;

        var enemyBuff = this.surgingGrit.getEnemyPower();
        this.entity = new SurgingGritEntity(this, enemyBuff);
    }

    public void increaseGridAmount(int amount) {
        int current = this.gridAmount;
        if (this.surgingGridActive) {
            this.gridOverflow = Math.min(this.gridAmount + amount, 30);
            return;
        }

        this.gridAmount = Math.min(this.gridAmount + amount, 100);
        this.addToLog(new GainGridPoints(current, this.gridAmount));

        if (this.gridAmount == 100) {
            this.activeSurgingGrid();
        }
    }

    private void activeSurgingGrid() {
        this.gridAmount = 0;
        this.surgingGridActive = true;

        this.pfBuff.applySurgingGritBuff(this);
        this.surgingGrit.apply(this);


        var enemyBuff = this.surgingGrit.getEnemyPower();
        if (enemyBuff != null) {
            getEnemies().forEach(e -> e.addPower(enemyBuff));
        }

        this.actionValueMap.put(entity, entity.getBaseAV());
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
        } else if (this.currentWave.hasBoss()) {
            var boss = this.currentWave.getBoss();
            float dmg = boss.maxHp() / this.currentWave.totalMinions();


            var res = boss.hitDirectly(new FixedHit(this.entity, boss, dmg));
            this.addToLog(new HitResultLine(res));
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

    @Override
    public String prefix() {
        return String.format("(%.2f AV / %d enemies left) - ", this.initialLength() - this.battleLength(), this.currentWave.size());
    }

    public static class SurgingGritEntity extends AbstractEntity {

        private final PfBattle pf;
        @Nullable
        private final PermPower enemyBuff;

        public SurgingGritEntity(PfBattle pf, @Nullable PermPower enemyBuff) {
            this.name = "Surging grid entity";
            this.pf = pf;
            this.enemyBuff = enemyBuff;
            this.setBattle(pf);
        }

        @Override
        public void onEnemyJoinCombat(AbstractEnemy enemy) {
            if (this.enemyBuff != null) {
                enemy.addPower(this.enemyBuff);
            }
        }

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
