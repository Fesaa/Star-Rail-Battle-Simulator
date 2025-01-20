package art.ameliah.hsr.enemies.game.cosmos;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.combat.hit.Hit;
import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyAction;
import art.ameliah.hsr.battleLogic.log.lines.enemy.GainedWeakness;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyAttackType;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

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
    public void beforeReceiveHit(Hit hit) {
        if (this.weaknessMap.size() >= 2) {
            return;
        }

        if (hit.getElementType() != null && this.addWeakness(hit.getElementType())) {
            getBattle().addToLog(new GainedWeakness(this, hit.getElementType()));
        }
    }

    @Override
    public void onWeaknessBreak(BattleParticipant source) {
        if (this.isDead()) {
            return;
        }
        this.currentHp.set(-1f);
    }

    @Override
    protected void act() {
        this.actionMetric.record(EnemyAttackType.SINGLE);
        this.startAttack().handle(da -> da.logic(getRandomTarget(), (c, al) -> {
                    al.hit(c, this.boostedATK);
                    getBattle().addToLog(new EnemyAction(this, c, EnemyAttackType.SINGLE));
                }))
                .afterAttackHook(() -> {
                    // CURSED
                    this.boostedATK += (float) (Math.pow(2, this.turnsMetric.get()) * this.boostedATK);
                }).execute();
    }
}
