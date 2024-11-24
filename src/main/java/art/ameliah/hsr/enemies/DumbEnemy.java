package art.ameliah.hsr.enemies;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.combat.EnemyAttack;
import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyAction;
import art.ameliah.hsr.battleLogic.log.lines.enemy.SecondAction;
import art.ameliah.hsr.characters.AbstractCharacter;

public class DumbEnemy extends AbstractEnemy {

    public final int doubleActionCooldown;
    public int doubleActionCounter;

    public DumbEnemy(String name, EnemyType type, int baseHP, int baseATK, int baseDEF, int baseSpeed, int toughness, int doubleActionCooldown) {
        super(name, type, baseHP, baseATK, baseDEF, baseSpeed, toughness, 95);
        this.doubleActionCooldown = doubleActionCooldown;
    }

    public void attack() {
        numAttacksMetric++;
        float dmgToDeal = this.attackDmg();

        EnemyAttackType attackType = rollAttackType();
        if (attackType == EnemyAttackType.AOE) {
            numAoEMetric++;
            getBattle().addToLog(new EnemyAction(this, null, EnemyAttackType.AOE));

            this.startAttack()
                    .hit(getBattle().getPlayers(), 10, dmgToDeal)
                    .execute();
            return;
        }

        int targetPos = this.getRandomTargetPosition();
        AbstractCharacter<?> target = getBattle().getPlayers().get(targetPos); // Fine to throw if pos is too large.

        if (attackType == EnemyAttackType.SINGLE) {
            numSingleTargetMetric++;
            getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.SINGLE));
            this.startAttack()
                    .hit(target, 10, dmgToDeal)
                    .execute();
        } else {
            numBlastMetric++;
            getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.BLAST));

            EnemyAttack attack = this.startAttack();
            attack.hit(target, 10, dmgToDeal);
            getBattle().characterCallback(targetPos + 1, t -> attack.hit(t, 10, dmgToDeal));
            getBattle().characterCallback(targetPos - 1, t -> attack.hit(t, 10, dmgToDeal));
            attack.execute();
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
    public void afterAttacked(Attack attack) {
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
