package art.ameliah.hsr.enemies;

import art.ameliah.hsr.battleLogic.combat.enemy.EnemyAttackLogic;
import art.ameliah.hsr.battleLogic.combat.hit.Hit;
import art.ameliah.hsr.battleLogic.combat.result.HitResult;
import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyAction;
import art.ameliah.hsr.battleLogic.log.lines.enemy.SecondAction;
import art.ameliah.hsr.characters.AbstractCharacter;

import java.util.function.BiConsumer;

public class DumbEnemy extends AbstractEnemy {

    public final int doubleActionCooldown;
    public int doubleActionCounter;

    public DumbEnemy(String name, EnemyType type, int baseHP, int baseATK, int baseDEF, int baseSpeed, int toughness, int doubleActionCooldown) {
        super(name, type, baseHP, baseATK, baseDEF, baseSpeed, toughness, 95);
        this.doubleActionCooldown = doubleActionCooldown;
    }

    public void attack() {
        float dmgToDeal = this.attackDmg();

        EnemyAttackType attackType = rollAttackType();
        if (attackType == EnemyAttackType.AOE) {
            this.actionMetric.record(EnemyAttackType.AOE);
            getBattle().addToLog(new EnemyAction(this, null, EnemyAttackType.AOE));

            this.doAttack(da -> da.logic(getBattle().getPlayers(), (c, al) -> al.hit(c, 10, dmgToDeal)));
            return;
        }

        if (attackType == EnemyAttackType.SINGLE) {
            this.actionMetric.record(EnemyAttackType.SINGLE);
            this.doAttack(da -> da.logic(this.getRandomTarget(), (e, al) -> {
                getBattle().addToLog(new EnemyAction(this, e, EnemyAttackType.SINGLE));
                al.hit(e, 10, dmgToDeal);
            }));
        } else {
            this.actionMetric.record(EnemyAttackType.BLAST);

            this.doAttack(da -> {
                var target = this.getRandomTarget();
                var targetPos = getBattle().getPlayers().indexOf(target);
                getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.BLAST));

                BiConsumer<AbstractCharacter<?>, EnemyAttackLogic> hitLogic = (c, al) -> al.hit(c, 10, dmgToDeal);

                da.logic(targetPos-1, hitLogic);
                da.logic(targetPos, hitLogic);
                da.logic(targetPos+1, hitLogic);
            });
        }
    }

    public EnemyAttackType rollAttackType() {
        double totalWeight = 0.0;
        for (EnemyAttackType type : EnemyAttackType.values()) {
            totalWeight += type.weight;
        }
        int idx = 0;
        for (double r = getBattle().getEnemyMoveRng().nextDouble() * totalWeight; idx < EnemyAttackType.values().length - 1; ++idx) {
            r -= EnemyAttackType.values()[idx].weight;
            if (r <= 0.0) break;
        }
        return EnemyAttackType.values()[idx];
    }

    // Prevent dumb enemy from dying
    @Override
    public HitResult hit(Hit hit) {
        return new HitResult(hit, this, hit.finalDmg(),
                this.decreaseToughness(hit.finalToughnessReduction(), hit.getSource()),
                this.isWeaknessBroken(),
                this.isDead()
        );
    }

    @Override
    protected void act() {
        this.attack();
        if (this.doubleActionCounter == 0) {
            getBattle().addToLog(new SecondAction(this));
            this.attack();
            this.doubleActionCounter = doubleActionCooldown;
        } else {
            this.doubleActionCounter--;
        }
    }
}
