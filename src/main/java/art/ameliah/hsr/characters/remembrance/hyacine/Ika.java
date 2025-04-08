package art.ameliah.hsr.characters.remembrance.hyacine;

import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.battleLogic.combat.hit.AllyHit;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.remembrance.Memosprite;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.HPGain;
import art.ameliah.hsr.events.character.HPLost;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.events.combat.TurnEndEvent;
import art.ameliah.hsr.metrics.CounterMetric;
import art.ameliah.hsr.powers.PermPower;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ika extends Memosprite<Ika, Hyacine> {

    public static final String NAME = "Ika";

    protected CounterMetric<Float> healingTally = metricRegistry.register(CounterMetric.newFloatCounter("ika::healing-tally"));

    public Ika(Hyacine master) {
        super(master, NAME, (int) (0.5 * master.getFinalHP()), 0, 80, ElementType.WIND,
                0, 100, Path.REMEMBRANCE);
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        getBattle().registerForPlayers(p -> {
            p.addPower(new HealingTallyTracker());
            p.addPower(new HoldingClearSkysHand());
        });
        getBattle().registerForEnemy(e -> {
            e.addPower(new HoldingClearSkysHand());
        });
    }

    @Override
    protected void memoSkill() {
        this.startAttack().handle(DamageType.MEMOSPRITE_DAMAGE, dl -> {
            dl.logic(getBattle().getEnemies(), (e, al) -> {
                for (var enemy : e) {
                    al.hit(AllyHit.fixedHit(al, this, enemy, 0.2f * this.healingTally.get(),
                            TOUGHNESS_DAMAGE_SINGLE_UNIT, ElementType.WIND, List.of(DamageType.MEMOSPRITE_DAMAGE)));
                }
            });
        }).afterAttackHook(() -> {
            this.healingTally.set(0.5f * this.healingTally.get());
        }).execute();
    }

    public class HoldingClearSkysHand extends PermPower {

        protected Set<AbstractCharacter<?>> toHeal = new HashSet<>();

        @Subscribe
        public void onHpLoss(HPLost e) {
            if (this.getOwner() instanceof AbstractCharacter<?> c) {
                this.toHeal.add(c);
            }
        }

        @Subscribe
        public void afterAction(TurnEndEvent e) {
            if (this.toHeal.isEmpty()) {
                return;
            }


            var amount = 0.02f * Ika.this.getMaster().getFinalHP() + 20;
            for (var c : this.toHeal) {
                c.increaseHealth(Ika.this, amount);

                if (Ika.this.getMaster().PetrichoricClearSkies.get() > 0) {
                    c.increaseHealth(Ika.this, amount);
                }
            }

            var decrease = this.toHeal.size() * 0.04f * Ika.this.getFinalHP();
            Ika.this.reduceHealth(this, decrease);
            this.toHeal.clear();
        }

    }

    public class HealingTallyTracker extends PermPower {

        @Subscribe
        public void afterHpGain(HPGain event) {
            if (!List.of(Ika.NAME, Hyacine.NAME).contains(event.getSource().getName())) {
                return;
            }

            Ika.this.healingTally.increase(event.getAmount());
        }

    }
}
