package enemies;

import battleLogic.log.lines.enemy.EnemyAction;
import battleLogic.log.lines.enemy.SecondAction;
import characters.AbstractCharacter;
import characters.DamageType;

import java.util.ArrayList;

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
            for (AbstractCharacter<?> character : getBattle().getPlayers()) {
                getBattle().getHelper().attackCharacter(this, character, 10, dmgToDeal);
            }
            return;
        }

        int targetPos = this.getRandomTargetPosition();
        AbstractCharacter<?> target = getBattle().getPlayers().get(targetPos); // Fine to throw if pos is too large.

        if (attackType == EnemyAttackType.SINGLE) {
            numSingleTargetMetric++;
            getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.SINGLE));
            getBattle().getHelper().attackCharacter(this, target, 10, dmgToDeal);
        } else {
            numBlastMetric++;
            getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.BLAST));
            getBattle().getHelper().attackCharacter(this, target, 10, dmgToDeal);
            if (targetPos + 1 < getBattle().getPlayers().size()) {
                getBattle().getHelper().attackCharacter(this, getBattle().getPlayers().get(targetPos + 1), 5, dmgToDeal);
            }
            if (targetPos - 1 >= 0) {
                getBattle().getHelper().attackCharacter(this, getBattle().getPlayers().get(targetPos - 1), 5,dmgToDeal );
            }
        }
    }

    public EnemyAttackType rollAttackType() {
        double totalWeight= 0.0;
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

    @Override
    public void onAttacked(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> types, int energyFromAttacked, float totalDmg) {
        this.currentHp -= totalDmg;

        if (this.currentHp <= 0) {
            //getBattle().removeEnemy(this);
            //getBattle().addToLog(new EnemyDied(this, character));
        }
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
