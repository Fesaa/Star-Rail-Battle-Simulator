package art.ameliah.hsr.enemies.game;

import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyAction;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyAttackType;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is the Aurumaton Spectral Envoy of MOC 11 Scalegorge Tidalflow
 */
public class AurumatonSpectralEnvoy extends AbstractEnemy {

    private static final double SubdueProc = 70;

    private AbstractCharacter<?> lockedOn;
    private boolean tryToLock = false;

    public AurumatonSpectralEnvoy() {
        super("Aurumaton Spectral Envoy", EnemyType.Elite, 448625, 685, 1000, 158.4f, 100, 92);
        this.addWeakness(ElementType.PHYSICAL);
        this.addWeakness(ElementType.LIGHTNING);
        this.addWeakness(ElementType.IMAGINARY);

        this.addPower(PermPower.create(PowerStat.EFFECT_HIT, 33.6f, "Base stat EHR"));
        this.addPower(PermPower.create(PowerStat.EFFECT_RES, 30, "Base stat ER"));

        this.sequence.addAction(this::Adjudicate, this::Subdue);
        this.sequence.addAction(this::Subdue, this::RevertYinAndYang, this::SoulWarrant);
    }

    @Override
    protected void act() {
        if (this.lockedOn != null) {
            this.HeavensFall(this.lockedOn);
            this.lockedOn = null;
            return;
        }

        if (this.tryToLock) {
            this.tryToLock = false;
            this.Adjudicate();
            return;
        }

        this.sequence.runNext();
    }

    private void Adjudicate() {
        AbstractCharacter<?> target = this.getRandomTarget();
        getBattle().getHelper().attackCharacter(this, target, 10, 976);
        getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.SINGLE, "Adjudicate"));
    }

    private void Subdue() {
        AbstractCharacter<?> target = this.getRandomTarget();
        getBattle().getHelper().attackCharacter(this, target, 10, 1171);

        if (getBattle().getEnemyEHRRng().nextDouble() * 100 > SubdueProc) {
            return;
        }

        target.addPower(new Reverberation());
        getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.SINGLE, "Subdue"));
    }

    private void RevertYinAndYang() {
        getBattle().addToLog(new EnemyAction(this, EnemyAttackType.AOE, "Revert Yin and Yang"));
        getBattle().getPlayers().forEach(p -> {
            getBattle().getHelper().attackCharacter(this, p, 10, 976);
        });

        long notReverberated = getBattle().getPlayers()
                .stream()
                .filter(p -> p.hasPower(Reverberation.NAME))
                .count();

        if (notReverberated > 2) {
            this.getRandomTargets(2).stream()
                    .filter(p -> this.successfulHit(p, 35))
                    .forEach(p -> p.addPower(new Reverberation()));
            return;
        }

        AbstractCharacter<?> target = this.getRandomTarget();
        if (this.successfulHit(target, 35)) {
            target.addPower(new Reverberation());
        }
    }

    private void HeavensFall(AbstractCharacter<?> target) {
        getBattle().getHelper().attackCharacter(this, target, 20, 976);

        if (target.hasPower(StrongReverberation.NAME)) {
            getBattle().getHelper().attackCharacter(this, target, 0, 976);
            target.removePower(StrongReverberation.NAME);
        }

        getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.SINGLE, "Heavens Fall"));
    }

    private void SoulWarrant() {
        List<AbstractCharacter<?>> inflicted = getBattle().getPlayers()
                .stream()
                .filter(p -> p.hasPower(StrongReverberation.NAME))
                .collect(Collectors.toList());
        if (!inflicted.isEmpty()) {
            this.lockedOn = inflicted.get(getBattle().getEnemyTargetRng().nextInt(inflicted.size()));
        }
        this.tryToLock = true;
        getBattle().addToLog(new EnemyAction(this, "Soul Warrant (Success=" + (this.lockedOn != null) + ")"));
    }

    public static class Reverberation extends TempPower {
        public static String NAME = "Reverberation";
        public Reverberation() {
            super(2, NAME);
        }

        @Override
        public void onAttacked(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> types, int energyFromAttacked, float totalDmg) {
            character.removePower(this);
            character.addPower(new StrongReverberation());
            getBattle().DelayEntity(character, 70);
        }
    }

    public static class StrongReverberation extends TempPower {
        public static String NAME = "Strong Reverberation";
        public StrongReverberation() {
            super(1, NAME);
        }
    }
}
