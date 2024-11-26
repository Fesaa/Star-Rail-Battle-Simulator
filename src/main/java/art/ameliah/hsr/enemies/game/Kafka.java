package art.ameliah.hsr.enemies.game;

import art.ameliah.hsr.battleLogic.combat.AttackLogic;
import art.ameliah.hsr.battleLogic.combat.EnemyAttack;
import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyAction;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyAttackType;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.dot.EnemyShock;
import art.ameliah.hsr.utils.Randf;

import java.util.concurrent.atomic.AtomicBoolean;

public class Kafka extends AbstractEnemy {

    private boolean cooldown;

    public Kafka() {
        super("Kafka", EnemyType.Boss, 1046791, 718, 1000, 157.08f, 150, 92);

        this.addWeakness(ElementType.PHYSICAL);
        this.addWeakness(ElementType.WIND);
        this.addWeakness(ElementType.IMAGINARY);
        this.setRes(ElementType.LIGHTNING, 40);

        this.addPower(PermPower.create(PowerStat.EFFECT_HIT, 33.6f, "Kafka EHR boost"));
        this.addPower(PermPower.create(PowerStat.EFFECT_RES, 30, "Kafka ER boost"));

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
        getBattle().getPlayers().forEach(p -> p.addPower(new Cruelty(this)));
    }

    @Override
    public void onTurnStart() {
        this.cooldown = false;
    }

    private void MidnightTumult() {
        AbstractCharacter<?> target = this.getRandomTarget();
        this.startAttack().hit(target, 10, 813).execute();
        target.addPower(new EnemyShock(this, 244, 3, 1));
        getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.SINGLE, "Midnight Tumult"));
    }

    private void CaressingMoonlight() {
        int idx = this.getRandomTargetPosition();

        EnemyAttack attack = this.startAttack();

        AtomicBoolean inflictShock = new AtomicBoolean(false);
        getBattle().characterCallback(idx, c -> {
            attack.hit(c, 10, 976);
            inflictShock.set(c.hasPower(EnemyShock.NAME));
        });
        getBattle().characterCallback(idx - 1, c -> {
            attack.hit(c, 10, 651); // Not sure about energy
            if (inflictShock.get()) {
                c.addPower(new EnemyShock(this, 244, 3, 1));
            }
        });
        getBattle().characterCallback(idx + 1, c -> {
            attack.hit(c, 10, 651); // Not sure about energy
            if (inflictShock.get()) {
                c.addPower(new EnemyShock(this, 244, 3, 1));
            }
        });

        attack.execute();

        getBattle().addToLog(new EnemyAction(this, getBattle().getCharacter(idx), EnemyAttackType.BLAST, "Caressing Moonlight"));
    }

    private void SilentAndSharpMockery() {
        this.startAttack().hit(getBattle().getPlayers(), 15, 813).execute();
        getBattle().addToLog(new EnemyAction(this, EnemyAttackType.AOE, "Silent and Sharp Mockery"));
    }

    private void SpiritWhisper() {
        AbstractCharacter<?> target = this.getRandomTarget();
        if (this.successfulHit(target, 120)) {
            target.addPower(new Dominating());
        }

        getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.SINGLE, "Spirit Whisper"));
        getBattle().AdvanceEntity(target, 100);
    }

    // Not actually implemented
    public static class Dominating extends TempPower {
        public static final String NAME = "Dominating";

        public Dominating() {
            super(3, NAME);
            this.type = PowerType.DEBUFF;
        }
    }

    public static class Cruelty extends PermPower {
        public static final String NAME = "Cruelty";
        private final Kafka kafka;

        public Cruelty(Kafka kafka) {
            super(NAME);
            this.kafka = kafka;
            this.type = PowerType.DEBUFF;
        }

        @Override
        public void afterAttacked(EnemyAttack attack) {
            AbstractCharacter<?> target = Randf.rand(attack.getTargets(), getBattle().getEnemyTargetRng());
            if (this.kafka.cooldown || !target.hasPower(EnemyShock.NAME)) {
                return;
            }


            this.kafka.cooldown = true;
            this.kafka.startAttack().hit(target, 10, 325).execute();
        }
    }

    public class ExtraShockDmg extends PermPower {
        public final String NAME = "ExtraShockDmg";

        public ExtraShockDmg() {
            this.name = name;
        }

        @Override
        public void beforeAttack(AttackLogic attack) {
            AbstractPower shock = attack.getSource().getPower(EnemyShock.NAME);
            if (shock == null) {
                return;
            }

            EnemyShock enemyShock = (EnemyShock) shock;
            Kafka.this.startAttack().hit(attack.getSource(), 0, enemyShock.getDmg()).execute();
        }
    }

}
