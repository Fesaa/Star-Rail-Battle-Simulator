package art.ameliah.hsr.enemies.game.penacony;

import art.ameliah.hsr.battleLogic.combat.AttackLogic;
import art.ameliah.hsr.battleLogic.combat.EnemyAttack;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.HashSet;
import java.util.Set;

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
        this.startAttack().hit(this.getRandomTarget(), 10, 784).execute();
    }

    private void AdmonishmentOfTheMasses() {
        int idx = this.getRandomTargetPosition();

        EnemyAttack attack = this.startAttack();
        getBattle().characterCallback(idx-1, c -> attack.hit(c, 10, 653));
        getBattle().characterCallback(idx, c -> attack.hit(c, 10, 653));
        getBattle().characterCallback(idx+1, c -> attack.hit(c, 10, 653));
        attack.execute();
    }

    private void ShackleBearingMessenger() {
        var firstTarget = this.getRandomTarget();
        this.lockedOn.clear();
        this.lockedOn.add(firstTarget);
        this.charging = true;
    }

    private void DesmiosEvangelion() {
        int locks = this.lockedOn.size();
        if (locks == 0) {
            return;
        }

        float dmgPerLock = 3267 / (float) locks;

        EnemyAttack attack = this.startAttack();
        for (var lock : this.lockedOn) {
            attack.hit(lock, 15, dmgPerLock);
        }
        attack.execute();

        this.lockedOn.clear();
        this.charging = false;
    }
}
