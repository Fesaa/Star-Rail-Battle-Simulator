package enemies.game;

import battleLogic.log.lines.enemy.EnemyAction;
import characters.AbstractCharacter;
import characters.DamageType;
import characters.ElementType;
import enemies.AbstractEnemy;
import enemies.EnemyAttackType;
import enemies.EnemyType;
import powers.AbstractPower;
import powers.PermPower;
import powers.TempPower;
import powers.dot.EnemyShock;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Kafka extends AbstractEnemy {

    private boolean cooldown;

    public Kafka() {
        super("Kafka", EnemyType.Boss, 1046791, 718, 1000, 157.08f, 150, 92);

        this.addWeakness(ElementType.PHYSICAL);
        this.addWeakness(ElementType.WIND);
        this.addWeakness(ElementType.IMAGINARY);

        this.sequence.addAction(this::MidnightTumult, this::SpiritWhisper);
        this.sequence.addAction(this::CaressingMoonlight, this::SilentAndSharpMockery);

        this.addPower(new ExtraShockDmg());
    }

    @Override
    protected void act() {
        this.sequence.runNext();
    }

    @Override
    public void onCombatStart() {
        super.onCombatStart();
        getBattle().getPlayers().forEach(p -> {
            p.addPower(new Cruelty(this));
        });
    }

    @Override
    public void onTurnStart() {
        this.cooldown = false;
    }

    private void MidnightTumult() {
        AbstractCharacter<?> target = this.getRandomTarget();
        getBattle().getHelper().attackCharacter(this, target, 10, 813);
        target.addPower(new EnemyShock(this, 244, 3, 1));
        getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.SINGLE, "Midnight Tumult"));
    }

    private void CaressingMoonlight() {
        int idx = this.getRandomTargetPosition();

        AtomicBoolean inflictShock = new AtomicBoolean(false);
        getBattle().characterCallback(idx, c -> {
            getBattle().getHelper().attackCharacter(this, c, 10, 976);
            inflictShock.set(c.hasPower(EnemyShock.NAME));
        });
        getBattle().characterCallback(idx-1, c -> {
            getBattle().getHelper().attackCharacter(this, c, 10, 651); // Not sure about energy
            if (inflictShock.get()) {
                c.addPower(new EnemyShock(this, 244, 3, 1));
            }
        });
        getBattle().characterCallback(idx+1, c -> {
            getBattle().getHelper().attackCharacter(this, c, 10, 651); // Not sure about energy
            if (inflictShock.get()) {
                c.addPower(new EnemyShock(this, 244, 3, 1));
            }
        });

        getBattle().addToLog(new EnemyAction(this, getBattle().getCharacter(idx), EnemyAttackType.BLAST, "Caressing Moonlight"));
    }

    private void SilentAndSharpMockery() {
        getBattle().getPlayers().forEach(p -> {
            getBattle().getHelper().attackCharacter(this, p, 15, 813); // Not sure about energy
        });

        getBattle().addToLog(new EnemyAction(this, EnemyAttackType.AOE, "Silent and Sharp Mockery"));
    }

    private void SpiritWhisper() {
        AbstractCharacter<?> target = this.getRandomTarget();
        if (this.successfulHit(target, 120)) {
            target.addPower(new Dominating());
        }

        getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.SINGLE, "Spirit Whisper"));
    }

    // Not actually implemented
    public static class Dominating extends TempPower {
        public static String NAME = "Dominating";

        public Dominating() {
            super(3, NAME);
        }
    }

    public class ExtraShockDmg extends PermPower {
        public String NAME = "ExtraShockDmg";

        public ExtraShockDmg() {
            this.name = NAME;
        }

        @Override
        public void onAttack(AbstractCharacter<?> character, ArrayList<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
        AbstractPower shock = character.getPower(EnemyShock.NAME);
        if (shock == null) {
            return;
        }

        EnemyShock enemyShock = (EnemyShock)shock;
        getBattle().getHelper().attackCharacter(Kafka.this, character, 0, enemyShock.getDmg());
        }
    }

    public static class Cruelty extends PermPower {
        public static String NAME = "Cruelty";
        private final Kafka kafka;

        public Cruelty(Kafka kafka) {
            super(NAME);
            this.kafka = kafka;
        }

        @Override
        public void onAttacked(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> types, int energyFromAttacked, float totalDmg) {
            if (this.kafka.cooldown || !character.hasPower(EnemyShock.NAME)) {
             return;
            }


            this.kafka.cooldown = true;
            getBattle().getHelper().attackCharacter(this.kafka, character, 10, 325);
        }
    }

}
