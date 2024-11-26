package art.ameliah.hsr.enemies.game.jarilovi;

import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.dot.EnemyBurn;

public class IncinerationShadewalker extends AbstractEnemy {

    private final static String NAME = "Incineration Shadewalker";

    public IncinerationShadewalker() {
        super(NAME, EnemyType.Minion, 68283, 718, 1150, 120, 20, 85);

        this.addWeakness(ElementType.ICE);
        this.addWeakness(ElementType.WIND);
        this.addWeakness(ElementType.IMAGINARY);

        this.setRes(ElementType.FIRE, 40);

        this.addPower(PermPower.create(PowerStat.EFFECT_HIT, 28, "Base stat EHR"));
        this.addPower(PermPower.create(PowerStat.EFFECT_RES, 20, "Base stat ER"));
    }

    @Override
    protected void act() {
        var target = this.getRandomTarget();
        this.startAttack().hit(target, 15, 653).execute();

        if (this.successfulHit(target, 100)) {
            target.addPower(new EnemyBurn(this, 108, 3));
        }

    }
}
