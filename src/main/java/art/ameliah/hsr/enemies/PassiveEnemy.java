package art.ameliah.hsr.enemies;

import art.ameliah.hsr.battleLogic.combat.hit.Hit;
import art.ameliah.hsr.battleLogic.combat.result.HitResult;

public class PassiveEnemy extends AbstractEnemy {

    public PassiveEnemy(String name, EnemyType type, int baseHP, int baseATK, int baseDEF, int baseSpeed, int toughness) {
        super(name, type, baseHP, baseATK, baseDEF, baseSpeed, toughness, 95);
    }

    // Prevent passive enemy from dying
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

    }
}
