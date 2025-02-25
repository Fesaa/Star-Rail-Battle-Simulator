package art.ameliah.hsr.enemies.game.jarilovi;

import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyAction;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyAttackType;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class Gepard extends AbstractEnemy {

    public static final String NAME = "Gepard";

    public Gepard() {
        super(NAME, EnemyType.Boss, 9244496, 718, 1150, 144, 160, 85);

        this.addWeakness(ElementType.PHYSICAL);
        this.addWeakness(ElementType.LIGHTNING);
        this.addWeakness(ElementType.IMAGINARY);

        this.addPower(PermPower.create(PowerStat.EFFECT_HIT, 28, "Base stat EHR"));
        this.addPower(PermPower.create(PowerStat.EFFECT_RES, 30, "Base stat ER"));

        // TODO: No idea if this is correct
        this.sequence.addAction(this::FistOfConviction, this::SmiteOfFrost);
        this.sequence.addAction(this::SiegeSupport, this::GarrisonAuraField);
        this.sequence.addAction(this::FrigidWaterfall);

    }

    @Override
    protected void act() {
        this.sequence.runNext();
    }

    private void FistOfConviction() {
        this.actionMetric.record(EnemyAttackType.SINGLE);
        this.doAttack(da -> da.logic(this.getRandomTarget(), (c, al) -> {
            al.hit(c, 10, 653);
            getBattle().addToLog(new EnemyAction(this, c, EnemyAttackType.SINGLE));
        }));
    }

    private void SmiteOfFrost() {
        this.actionMetric.record(EnemyAttackType.SINGLE);
        this.doAttack(da -> da.logic(this.getRandomTarget(), (c, al) -> {
            al.hit(c, 15, 980);
            getBattle().addToLog(new EnemyAction(this, c, EnemyAttackType.SINGLE));
        }));
    }

    private void FrigidWaterfall() {
        this.actionMetric.record(EnemyAttackType.AOE);
        this.doAttack(da -> da.logic(getBattle().getPlayers(), (c, al) -> {
            al.hit(c, 15, 762);
            getBattle().addToLog(new EnemyAction(this, null, EnemyAttackType.AOE));
        }));
    }

    private void SiegeSupport() {
        // TODO: Implement (but it's fine, PF will be full anyway)
    }

    private void GarrisonAuraField() {
        // TODO: Implement, this currently isn't possible.
        // Shouldn't be too hard, just add some method in powers to cancel hits ig
    }
}
