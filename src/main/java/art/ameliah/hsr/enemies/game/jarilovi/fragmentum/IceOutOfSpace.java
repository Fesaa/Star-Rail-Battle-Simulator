package art.ameliah.hsr.enemies.game.jarilovi.fragmentum;

import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyAction;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyAttackType;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.dot.EnemyFrozen;

public class IceOutOfSpace extends AbstractEnemy {

    private boolean freezingPointState = false;

    public IceOutOfSpace() {
        super("Ice Out of Space", EnemyType.Elite, 373854, 718, 1000, 132, 100, 92);
        this.addWeakness(ElementType.FIRE);
        this.addWeakness(ElementType.WIND);
        this.addWeakness(ElementType.QUANTUM);
        this.setRes(ElementType.ICE, 40);

        this.addPower(PermPower.create(PowerStat.EFFECT_RES, 30, "Ice Out of Space ER Boost"));
        this.addPower(PermPower.create(PowerStat.EFFECT_HIT, 33.6f, "Ice Out of Space EHR Boost"));
    }

    @Override
    protected void act() {
        if (this.freezingPointState) {
            this.ChillingLament();
            this.EverwinterRain();
            this.freezingPointState = false;
            return;
        }

        this.ChillingLament();
        this.FrostyAbsorption();
    }

    private void ChillingLament() {
        this.actionMetric.record(EnemyAttackType.SINGLE);
        this.doAttack(da -> da.logic(this.getRandomTarget(), (c, al) -> {
            al.hit(c, 10, 813);

            // Guessing it works like this, but not certain
            if (this.freezingPointState && this.successfulHit(c, 60)) {
                c.addPower(new EnemyFrozen(this, 1, 325));
            }

            if (this.freezingPointState && c.hasPower(EnemyFrozen.NAME)) {
                al.hit(c, 325);
            }

            getBattle().addToLog(new EnemyAction(this, c, EnemyAttackType.SINGLE, "Chilling Lament"));
        }));
    }

    private void FrostyAbsorption() {
        this.freezingPointState = true;
        getBattle().addToLog(new EnemyAction(this, "Frosty Absorption"));
    }

    private void EverwinterRain() {
        this.actionMetric.record(EnemyAttackType.AOE);
        this.doAttack(da -> da.logic(getBattle().getPlayers(), (c, al) -> {
            al.hit(c, 15, 651);

            getBattle().getPlayers()
                    .stream()
                    .filter(p -> !p.hasPower(EnemyFrozen.NAME))
                    .sorted((_, _) -> getBattle().getEnemyTargetRng().nextInt(-1, 2))
                    .limit(2)
                    .filter(p -> this.successfulHit(p, 60))
                    .forEach(p -> p.addPower(new EnemyFrozen(this, 1, 325)));

            getBattle().addToLog(new EnemyAction(this, EnemyAttackType.AOE, "Everwinter Rain"));
        }));
    }
}

