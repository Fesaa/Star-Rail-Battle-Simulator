package art.ameliah.hsr.enemies.game;

import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyAction;
import art.ameliah.hsr.characters.AbstractCharacter;
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
        AbstractCharacter<?> target = this.getRandomTarget();
        this.startAttack().hit(target, 15, 976).execute();
        getBattle().DelayEntity(target, 50);
        getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.SINGLE, "Frost Crush"));
    }
}
