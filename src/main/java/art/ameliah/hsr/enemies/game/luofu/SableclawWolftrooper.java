package art.ameliah.hsr.enemies.game.luofu;

import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyAction;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyAttackType;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.enemies.game.luofu.moonrage.MoonRageAble;
import art.ameliah.hsr.enemies.game.luofu.moonrage.MoonRageTracker;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.events.combat.EnemyJoinCombat;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.dot.EnemyBleed;

public class SableclawWolftrooper extends AbstractEnemy implements MoonRageAble {

    public final static String NAME = "Sableclaw Wolftrooper";

    private final PermPower lupineMaw = new MoonRageTracker();
    // TODO: Check number
    private final PermPower moonRageSPD = PermPower.create(PowerStat.SPEED_PERCENT, 15, NAME + " Moon Rage SPD Boost");
    private boolean inMoonRage = false;

    public SableclawWolftrooper() {
        super(NAME, EnemyType.Minion, 91044, 718, 1150, 144, 30, 85);

        this.addWeakness(ElementType.FIRE);
        this.addWeakness(ElementType.IMAGINARY);

        this.addPower(PermPower.create(PowerStat.EFFECT_HIT, 28, "Base stat EHR"));
        this.addPower(PermPower.create(PowerStat.EFFECT_RES, 20, "Base stat ER"));
    }

    @Subscribe
    public void onCombatStartAddLupine(CombatStartEvent event) {
        getBattle().getEnemies().forEach(e -> e.addPower(this.lupineMaw));
    }

    @Subscribe
    public void onEnemyJoinCombat(EnemyJoinCombat event) {
        event.getEnemy().addPower(this.lupineMaw);
    }

    @Override
    protected void act() {
        this.actionMetric.record(EnemyAttackType.SINGLE);
        this.startAttack().handle(da -> da.logic(this.getRandomTarget(), (c, al) -> {
            al.hit(c, 10, this.attackDmg());
            if (this.inMoonRage && this.successfulHit(c, 75)) { // check chance
                c.addPower(new EnemyBleed());
            }
            getBattle().addToLog(new EnemyAction(this, c, EnemyAttackType.SINGLE));
        })).execute();
    }

    @Override
    public void enterMoonRage() {
        this.inMoonRage = true;
        getBattle().IncreaseSpeed(this, moonRageSPD);
    }

    @Override
    public void dispelMoonRage() {
        this.inMoonRage = false;
    }

    @Override
    public int maxBloodLustStacks() {
        return 7; // TODO: double check
    }
}
