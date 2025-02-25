package art.ameliah.hsr.enemies.game.stellaronhunters;

import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyAction;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyAttackType;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostAllyAttacked;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.events.combat.TurnStartEvent;
import art.ameliah.hsr.events.enemy.PreEnemyAttack;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.dot.EnemyShock;
import art.ameliah.hsr.utils.Randf;

import java.util.concurrent.atomic.AtomicBoolean;

public class Kafka extends AbstractEnemy {

    private boolean cooldown;

    public Kafka(int baseHp) {
        super("Kafka", EnemyType.Boss, baseHp, 738, 1000, 157.08f, 150, 95);

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

    @Subscribe
    public void onCombatStartKafka(CombatStartEvent e) {
        getBattle().registerForPlayers(p -> p.addPower(new Cruelty(this)));
    }

    @Subscribe
    public void onTurnStartResetCooldown(TurnStartEvent event) {
        this.cooldown = false;
    }

    private void MidnightTumult() {
        this.actionMetric.record(EnemyAttackType.SINGLE);
        this.doAttack(da -> da.logic(this.getRandomTarget(), (c, al) -> {
            al.hit(c, 10, 986);
            c.addPower(new EnemyShock(this, 296, 3, 1));
            getBattle().addToLog(new EnemyAction(this, c, EnemyAttackType.SINGLE, "Midnight Tumult"));
        }));
    }

    private void CaressingMoonlight() {
        this.actionMetric.record(EnemyAttackType.BLAST);
        this.doAttack(da -> {
            int idx = this.getRandomTargetPosition();

            AtomicBoolean inflictShock = new AtomicBoolean(false);
            da.logic(idx, (c, al) -> {
                al.hit(c, 10, 1184);
                inflictShock.set(c.hasPower(EnemyShock.NAME));
            });
            da.logic(idx-1, (c, al) -> {
                al.hit(c, 10, 789);
                if (inflictShock.get()) {
                    c.addPower(new EnemyShock(this, 244, 3, 1));
                }
            });
            da.logic(idx+1, (c, al) -> {
                al.hit(c, 10, 789);
                if (inflictShock.get()) {
                    c.addPower(new EnemyShock(this, 244, 3, 1));
                }
            });

            getBattle().addToLog(new EnemyAction(this, getBattle().getCharacter(idx), EnemyAttackType.BLAST, "Caressing Moonlight"));
        });
    }

    private void SilentAndSharpMockery() {
        this.actionMetric.record(EnemyAttackType.AOE);
        this.doAttack(da -> da.logic(getBattle().getPlayers(), (c, al) -> {
            al.hit(c, 15, 986);
            getBattle().addToLog(new EnemyAction(this, EnemyAttackType.AOE, "Silent and Sharp Mockery"));
        }));
    }

    private void SpiritWhisper() {
        AbstractCharacter<?> target = this.getRandomTarget();
        if (this.successfulHit(target, 120)) {
            target.addPower(new Dominating());
        }

        this.actionMetric.record(EnemyAttackType.SINGLE);
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

        @Subscribe
        public void afterAttacked(PostAllyAttacked e) {
            AbstractCharacter<?> target = Randf.rand(e.getAttack().getTargets(), getBattle().getEnemyTargetRng());
            if (target == null) {
                return;
            }
            if (this.kafka.cooldown || !target.hasPower(EnemyShock.NAME)) {
                return;
            }


            this.kafka.cooldown = true;
            this.kafka.doAttack(da -> da.logic(target, al -> al.hit(target, 10, 394)));
        }
    }

    public class ExtraShockDmg extends PermPower {
        public final String NAME = "ExtraShockDmg";

        public ExtraShockDmg() {
            this.name = NAME;
        }

        @Subscribe
        public void beforeAttack(PreEnemyAttack e) {
            /*e.getAttack().getTargets().forEach(t -> {
                AbstractPower shock = t.getPower(EnemyShock.NAME);
                if (shock == null) {
                    return;
                }

                EnemyShock enemyShock = (EnemyShock) shock;
                Kafka.this.doAttack(da -> da.logic(al -> {
                    al.hit(t, enemyShock.getDmg());
                }));
            });*/
        }
    }

}
