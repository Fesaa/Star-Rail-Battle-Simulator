package art.ameliah.hsr.characters.nihility.cipher;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.battleLogic.combat.result.HitResult;
import art.ameliah.hsr.battleLogic.log.lines.entity.GainCharge;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.Eidolon;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostAllyAttack;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.events.combat.DeathEvent;
import art.ameliah.hsr.events.combat.WaveStartEvent;
import art.ameliah.hsr.events.enemy.PostEnemyAttacked;
import art.ameliah.hsr.metrics.CounterMetric;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;
import art.ameliah.hsr.utils.Comparators;
import lombok.extern.log4j.Log4j2;

import java.util.function.BiConsumer;

@Log4j2
public class Cipher extends AbstractCharacter<Cipher> {

    public static final String NAME = "Cipher";

    protected final CounterMetric<Double> tally = metricRegistry.register(CounterMetric.newDoubleCounter("cipher::talent-tally"));
    protected final CounterMetric<Integer> fuaCount = metricRegistry.register(CounterMetric.newIntegerCounter("cipher::talent-fua-count"));

    public Cipher() {
        super(NAME, 931, 640, 509, 106, 80, ElementType.QUANTUM, 100, 100, Path.NIHILITY);

        this.addPower(new TracePower()
                .setStat(PowerStat.FLAT_SPEED, 14)
                .setStat(PowerStat.QUANTUM_DMG_BOOST, 14)
                .setStat(PowerStat.HP_PERCENT, 10));
        this.hasAttackingUltimate = true;

        this.addPower(new InsightForSmiles());

    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        getBattle().getEnemies().stream()
                .max(Comparators::CompareHealth)
                .orElseThrow()
                .addPower(new HospitableDolos());

        getBattle().registerForEnemy(e -> {
            e.addPower(PermPower.create(PowerStat.DEFENSE_REDUCTION, 30, "Ultimate Switcheroo"));
            e.addPower(new Rogues());
        });
    }

    @Subscribe
    public void onWaveStart(WaveStartEvent e) {
        getBattle().AdvanceEntity(this, 10);
    }

    @Override
    protected void useBasic() {
        this.doAttack(DamageType.BASIC, dl -> {
            dl.logic(this.getTarget(MoveType.BASIC), (e, al) -> {
                al.hit(e, 1.2f, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            });
        });
    }

    @Override
    protected void useSkill() {
        this.doAttack(DamageType.SKILL, dl -> {
            var idx = this.getTargetIdx(MoveType.SKILL);

            this.addPower(TempPower.create(PowerStat.ATK_PERCENT, 30, 2, "Heh, Empty-Handed Haul"));

            dl.logic(idx-1, this.skillHit(1, TOUGHNESS_DAMAGE_SINGLE_UNIT));
            dl.logic(idx, this.skillHit(2, TOUGHNESS_DAMAGE_TWO_UNITS));
            dl.logic(idx+1, this.skillHit(1, TOUGHNESS_DAMAGE_SINGLE_UNIT));
        });
    }

    private BiConsumer<AbstractEnemy, AttackLogic> skillHit(float mul, float t) {
        return (e, al) -> {
            if (this.successFullHit(120, e)) {
                e.addPower(TempPower.create(PowerStat.DAMAGE_DOWN, 10, 2, "Heh, Empty-Handed Haul"));
            }

            al.hit(e, mul, t);
        };
    }

    @Override
    protected void useUltimate() {
        this.startAttack().handle(DamageType.ULTIMATE, dl -> {
            var idx = this.getTargetIdx(MoveType.ULTIMATE);

            dl.logic(idx, this.ultHit(1.25f, TOUGHNESS_DAMAGE_THREE_UNITs));

            dl.logic(idx-1, this.ultHit(0.4f, TOUGHNESS_DAMAGE_TWO_UNITS));
            dl.logic(idx, this.ultHit(0.4f, 0));
            dl.logic(idx+1, this.ultHit(0.4f, TOUGHNESS_DAMAGE_TWO_UNITS));
        }).afterAttackHook(() -> {
            this.tally.set((double) 0);
            this.fuaCount.set(eidolon.isActivated(Eidolon.E1) ? 3 : 1);
        }).execute();
    }

    private BiConsumer<AbstractEnemy, AttackLogic> ultHit(float mul, float t) {
        return (e, al) -> {
            al.hit(e, mul, t);
            al.hitFixed(Cipher.this, e, (float) (0.25f * tally.get()));
        };
    }

    public static class GodstepMarvelousShoes extends PermPower {
        public GodstepMarvelousShoes() {
            super("Godstep Marvelous Shoes");

            this.setConditionalStat(PowerStat.CRIT_CHANCE, c -> {
                if (c.getFinalSpeed() >= 170) {
                    return 50f;
                }

                if (c.getFinalSpeed() >= 140) {
                    return 25f;
                }

                return 0f;
            });
        }
    }

    public class InsightForSmiles extends PermPower {

        public InsightForSmiles() {
            super("Insight for Smiles");
        }

        @Subscribe
        public void afterAttack(PostAllyAttack e) {
            if (!eidolon.isActivated(Eidolon.E1)) {
                return;
            }

            e.getAttack().getTargets().forEach(target -> {
                if (successFullHit(120, target)) {
                    target.addPower(TempPower.create(PowerStat.DAMAGE_TAKEN, 25, 2, "Insight for Smiles"));
                }
            });
        }

    }

    public class Rogues extends PermPower {
        public Rogues() {
            super("Rogues");
        }

        @Subscribe
        public void afterAttacked(PostEnemyAttacked e) {
            if (this.getOwner().hasPower(HospitableDolos.NAME)) {
                return;
            }

            var dmg = e.getAttack().getAttack().getHitResults().stream()
                    .filter(hitRes -> !hitRes.getHit().getTypes().contains(DamageType.TRUE_DAMAGE))
                    .mapToDouble(HitResult::getDmgDealt)
                    .sum();

            var tallyCount = dmg * 0.05f * HospitableDolos.getGodstepMarvelousShoesMul(Cipher.this);
            tally.increase(tallyCount);
            getBattle().addToLog(new GainCharge(Cipher.this, tally, "Hospitable Dolos Tally"));
        }
    }

    public class HospitableDolos extends PermPower {

        public static final String NAME = "Hospitable Dolos";

        public HospitableDolos() {
            super(NAME);
        }

        @Subscribe
        public void onDeath(DeathEvent deathEvent) {
            var next = getBattle().getEnemies().stream()
                    .max(Comparators::CompareHealth);

            next.ifPresentOrElse(e -> e.addPower(this), () -> {
                log.warn("No new enemy to become Regular Customer");
            });


        }

        @Subscribe
        public void afterAttacked(PostEnemyAttacked e) {
            var dmg = e.getAttack().getAttack().getHitResults().stream()
                    .filter(hitRes -> !hitRes.getHit().getTypes().contains(DamageType.TRUE_DAMAGE))
                    .mapToDouble(HitResult::getDmgDealt)
                    .sum();

            var tallyCount = dmg * 0.15f * getGodstepMarvelousShoesMul(Cipher.this);
            tally.increase(tallyCount);
            getBattle().addToLog(new GainCharge(Cipher.this, tally, "Hospitable Dolos Tally"));

            if (fuaCount.get() > 0 && e.getAttack().getSource() != Cipher.this) {
                this.doTalentFua();
            }
        }

        public static float getGodstepMarvelousShoesMul(Cipher c) {
            if (c.getFinalSpeed() >= 170) {
                return 2f;
            }

            if (c.getFinalSpeed() >= 140) {
                return 1.5f;
            }

            return 1f;
        }

        private void doTalentFua() {
            fuaCount.decrement();

            Cipher.this.doAttack(DamageType.FOLLOW_UP, dl -> {
                dl.logic((AbstractEnemy) this.getOwner(), (e, al) -> {
                    al.hit(e, 4, TOUGHNESS_DAMAGE_HALF_UNIT);;
                });
            });
        }

    }
}
