package art.ameliah.hsr.enemies.game.jarilovi;

import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyAction;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyAttackType;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.events.enemy.PostEnemyAttack;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.utils.Randf;

public class SilvermaneCannoneer extends AbstractEnemy {
    public SilvermaneCannoneer() {
        super("Silvermane Cannoneer", EnemyType.Minion, 235314, 608, 1050, 120, 30, 85);

        this.addWeakness(ElementType.ICE);
        this.addWeakness(ElementType.PHYSICAL);
        this.addWeakness(ElementType.IMAGINARY);

        this.addPower(PermPower.create(PowerStat.EFFECT_HIT, 28, "Base stat EHR"));
        this.addPower(PermPower.create(PowerStat.EFFECT_RES, 20, "Base stat ER"));

        // TODO: Check ingame
        this.sequence.addAction(this::Barrage);
        this.sequence.addAction(this::CoveringSupport);
    }

    @Subscribe
    public void onCombatStartCoverSupport(CombatStartEvent event) {
        this.CoveringSupport();
    }

    @Override
    protected void act() {
        this.sequence.runNext();
    }

    private void Barrage() {
        this.actionMetric.record(EnemyAttackType.BLAST);
        this.doAttack(da -> {
            int idx = this.getRandomTargetPosition();

            da.logic(idx-1, (c, al) -> al.hit(c, 10, 217));
            da.logic(idx+1, (c, al) -> al.hit(c, 10, 217));
            da.logic(idx, (c, al) -> {
                al.hit(c, 10, 217);
                getBattle().addToLog(new EnemyAction(this, c, EnemyAttackType.BLAST));
            });
        });
    }

    private void CoveringSupport() {
        var target = getBattle().getRandomEnemy();
        target.addPower(new CoveringSupportPower());
    }

    public class CoveringSupportPower extends TempPower {

        public CoveringSupportPower() {
            super(1);
        }

        @Subscribe
        public void afterAttack(PostEnemyAttack e) {
            this.getOwner().removePower(this);
            SilvermaneCannoneer.this.actionMetric.record(EnemyAttackType.BLAST);
            SilvermaneCannoneer.this.doAttack(da -> {
                AbstractCharacter<?> target = Randf.rand(e.getAttack().getTargets(), getBattle().getEnemyTargetRng());
                int idx = getBattle().getPlayers().indexOf(target);

                da.logic(target, (c, al) -> al.hit(c, 12, 479));
                da.logic(idx+1, (c, al) -> al.hit(c, 12, 326));
                da.logic(idx-1, (c, al) -> al.hit(c, 12, 326));
                getBattle().addToLog(new EnemyAction(SilvermaneCannoneer.this, target, EnemyAttackType.BLAST));
            });
        }
    }
}
