package art.ameliah.hsr.enemies.game.jarilovi;

import art.ameliah.hsr.battleLogic.combat.AttackLogic;
import art.ameliah.hsr.battleLogic.combat.hit.Hit;
import art.ameliah.hsr.battleLogic.combat.result.HitResult;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class AutomatonBeetle extends AbstractEnemy {

    private boolean isInvincible = false;


    public AutomatonBeetle() {
        super("Automaton Beetle", EnemyType.Minion, 24512, 608, 1050, 120, 20, 85);

        this.addWeakness(ElementType.WIND);
        this.addWeakness(ElementType.LIGHTNING);
        this.addWeakness(ElementType.IMAGINARY);

        this.addPower(PermPower.create(PowerStat.EFFECT_HIT, 28, "Base stat EHR"));
        this.addPower(PermPower.create(PowerStat.EFFECT_RES, 20, "Base stat ER"));

    }

    @Override
    public HitResult hit(Hit hit) {
        if (this.isInvincible && !hit.getTypes().contains(DamageType.DOT)) {
            return new HitResult(hit, this, 0, 0, false, false);
        }

        return super.hit(hit);
    }

    @Override
    public void afterAttacked(AttackLogic attack) {
        this.isInvincible = false;
        super.afterAttacked(attack);
    }

    @Override
    protected void act() {
        this.startAttack()
                .hit(getRandomTarget(), 15, 653)
                .execute();

        this.isInvincible = true;
    }
}
