package art.ameliah.hsr.enemies.game.cosmos;

import art.ameliah.hsr.battleLogic.BattleEvents;
import art.ameliah.hsr.battleLogic.combat.Hit;
import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyDied;
import art.ameliah.hsr.battleLogic.log.lines.enemy.GainedWeakness;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.Set;

public class LordyTrashcan extends AbstractEnemy {
    private float boostedATK;

    // Different baseATK is used then ""advertised"" to match the logic nicer
    // TODO: FIX, see lookies
    public LordyTrashcan() {
        super("Lordy Trashcan", EnemyType.Minion, 175085, 871, 1050, 172.8f, 120, 85);

        this.addPower(PermPower.create(PowerStat.EFFECT_HIT, 28, "Base stat EHR"));
        this.addPower(PermPower.create(PowerStat.EFFECT_RES, 20, "Base stat ER"));

        this.boostedATK = this.baseATK;
    }

    @Override
    public void onBeforeHitEnemy(Hit hit) {
        if (this.weaknessMap.size() > 2) {
            return;
        }

        if (hit.getElementType() != null && this.addWeakness(hit.getElementType())) {
            getBattle().addToLog(new GainedWeakness(this, hit.getElementType()));
        }
    }

    @Override
    public void onWeaknessBreak() {
        if (this.isDead()) {
            return;
        }
        this.currentHp = -1;
    }

    @Override
    protected void act() {
        this.startAttack()
                .hit(getRandomTarget(), this.boostedATK)
                .execute();

        // CURSED
        this.boostedATK += (float) (Math.pow(2, this.numTurnsMetric) * this.boostedATK);
    }
}
