package art.ameliah.hsr.enemies.game.penacony;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.battleLogic.combat.enemy.EnemyAttack;
import art.ameliah.hsr.battleLogic.combat.enemy.EnemyAttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyAttackType;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class PastConfinedAndCaged extends AbstractEnemy {

    private final static String NAME = "Past Confined and Caged";

    private final Set<AbstractCharacter<?>> lockedOn = new HashSet<>();
    private boolean charging = false;


    public PastConfinedAndCaged() {
        super(NAME, EnemyType.Elite, 3755576, 718, 1150, 120, 120, 85);

        this.addWeakness(ElementType.FIRE);
        this.addWeakness(ElementType.LIGHTNING);
        this.addWeakness(ElementType.IMAGINARY);


        this.addPower(PermPower.create(PowerStat.EFFECT_HIT, 28, "Base stat EHR"));
        this.addPower(PermPower.create(PowerStat.EFFECT_RES, 30, "Base stat ER"));

        this.sequence.addAction(this::CleansingFlagellation);
        this.sequence.addAction(this::AdmonishmentOfTheMasses);
        this.sequence.addAction(this::ShackleBearingMessenger);
        this.sequence.addAction(this::DesmiosEvangelion);
    }

    @Override
    public void onWeaknessBreak() {
        if (this.charging) {
            getBattle().DelayEntity(this, 50); // TODO: No idea if this number is correct
        }
    }

    @Override
    public void afterAttacked(AttackLogic attack) {
        if (this.charging) {
            this.lockedOn.add(attack.getSource());
        }

        super.afterAttacked(attack);
    }

    @Override
    protected void act() {
        this.sequence.runNext();
    }

    private void CleansingFlagellation() {
        this.actionMetric.record(EnemyAttackType.SINGLE);
        this.doAttack(da -> da.logic(this.getRandomTarget(), (c, al) -> al.hit(c, 10, 784)));
    }

    private void AdmonishmentOfTheMasses() {
        this.actionMetric.record(EnemyAttackType.BLAST);
        this.doAttack(da -> {
            int idx = this.getRandomTargetPosition();
            BiConsumer<AbstractCharacter<?>, EnemyAttackLogic> l = (c, al) -> al.hit(c, 10, 653);

            da.logic(idx-1, l);
            da.logic(idx, l);
            da.logic(idx+1, l);
        });
    }

    private void ShackleBearingMessenger() {
        var firstTarget = this.getRandomTarget();
        this.lockedOn.clear();
        this.lockedOn.add(firstTarget);
        this.charging = true;
    }

    private EnemyAttackType typeForLock() {
        return switch (this.lockedOn.size()) {
            case 1 -> EnemyAttackType.SINGLE;
            case 2, 3 -> EnemyAttackType.BLAST;
            default -> EnemyAttackType.AOE;
        };
    }

    private void DesmiosEvangelion() {
        int locks = this.lockedOn.size();
        if (locks == 0) {
            return;
        }

        float dmgPerLock = 3267 / (float) locks;

        this.actionMetric.record(this.typeForLock());
        this.startAttack().handle(da -> {
            da.logic(this.lockedOn, (c, al) -> {
                al.hit(c, 15, dmgPerLock);
            });
        }).afterAttackHook(() -> {
            this.lockedOn.clear();
            this.charging = false;
        }).execute();
    }
}
