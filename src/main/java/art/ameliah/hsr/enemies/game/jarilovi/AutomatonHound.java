package art.ameliah.hsr.enemies.game.jarilovi;

import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyAttackType;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class AutomatonHound extends AbstractEnemy {

    public static final String NAME = "Automaton Hound";

    public AutomatonHound() {
        super(NAME, EnemyType.Minion, 156876, 608, 1050, 120, 20, 85);

        this.addWeakness(ElementType.PHYSICAL);
        this.addWeakness(ElementType.LIGHTNING);

        this.addPower(PermPower.create(PowerStat.EFFECT_HIT, 28, "Base stat EHR"));
        this.addPower(PermPower.create(PowerStat.EFFECT_RES, 20, "Base stat ER"));

        this.sequence.addAction(this::VerticalStrike);
        this.sequence.addAction(this::SelfHealingModule);
    }

    @Override
    protected void act() {
        this.sequence.runNext();
    }


    private void VerticalStrike() {
        this.actionMetric.record(EnemyAttackType.SINGLE);
        this.doAttack(da -> da.logic(this.getRandomTarget(), (c, al) -> al.hit(c, 15, 544)));
    }

    private void SelfHealingModule() {
        getBattle().getEnemies().forEach(e -> {
            e.getCurrentHp().set(e.getCurrentHp().get() + 0.25f * e.maxHp());
        });
    }
}
