package art.ameliah.hsr.enemies.game.jarilovi.fragmentum;

import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyAction;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyAttackType;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class EverwinterShadewalker extends AbstractEnemy {
    public static final String NAME = "Everwinter Shadewalker";

    public EverwinterShadewalker() {
        super(NAME, EnemyType.Minion, 56078, 718, 1000, 132, 20, 92);

        this.addWeakness(ElementType.PHYSICAL);
        this.addWeakness(ElementType.FIRE);
        this.addWeakness(ElementType.QUANTUM);
        this.setRes(ElementType.ICE, 40);

        this.addPower(PermPower.create(PowerStat.EFFECT_RES, 20, "Everwinter Shadewalker ER Boost"));
        this.addPower(PermPower.create(PowerStat.EFFECT_HIT, 33.6f, "Everwinter Shadewalker EHR Boost"));
    }

    @Override
    protected void act() {
        this.actionMetric.record(EnemyAttackType.SINGLE);
        this.doAttack(da -> da.logic(this.getRandomTarget(), (c, al) -> {
            al.hit(c, 15, 976);

            getBattle().DelayEntity(c, 50);
            getBattle().addToLog(new EnemyAction(this, c, EnemyAttackType.SINGLE, "Frost Crush"));
        }));
    }
}
