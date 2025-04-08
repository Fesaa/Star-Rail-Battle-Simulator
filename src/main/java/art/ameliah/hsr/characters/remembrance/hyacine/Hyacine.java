package art.ameliah.hsr.characters.remembrance.hyacine;

import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.Eidolon;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.remembrance.Memomaster;
import art.ameliah.hsr.characters.remembrance.Memosprite;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.MemospriteDeath;
import art.ameliah.hsr.events.character.PostAllyAttack;
import art.ameliah.hsr.events.character.PostSummon;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.events.combat.TurnEndEvent;
import art.ameliah.hsr.events.combat.TurnStartEvent;
import art.ameliah.hsr.metrics.CounterMetric;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TracePower;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Hyacine extends Memomaster<Hyacine> {

    public static final String NAME = "Hyacine";

    private Ika ika;
    private boolean firstSummon = true;

    protected CounterMetric<Integer> PetrichoricClearSkies = metricRegistry.register(CounterMetric.newIntegerCounter("hyacine::petrichoric-clear-skies"));

    public Hyacine() {
        super(NAME, 1087, 388, 631, 110, 80, ElementType.WIND,
                150, 100, Path.REMEMBRANCE);

        this.addPower(new TracePower()
                .setStat(PowerStat.FLAT_SPEED, 14)
                .setStat(PowerStat.EFFECT_RES, 18)
                .setStat(PowerStat.HP_PERCENT, 10));

        this.addPower(PermPower.create(PowerStat.EFFECT_RES, 50, "Gentle Thunderstorm"));
        this.addPower(new CloudyGrin());
        this.addPower(new TempestuousHalt());
    }

    @Override
    protected void summonMemo() {
        if (this.ika != null) {
            return;
        }

        this.ika = new Ika(this);
        this.ika.addPower(new CloudyGrin());
        this.ika.addPower(new TempestuousHalt());

        this.increaseEnergy(15, "Ika summoned");
        if (this.firstSummon) {
            this.increaseEnergy(30, "Ika summoned for the first time");
            this.firstSummon = false;
        }

        int idx = getBattle().getPlayers().indexOf(this);
        getBattle().addPlayerAt(this.ika, idx + 1);
        // Ika does not take actions normally.
        getBattle().getActionValueMap().remove(this.ika);

        this.eventBus.fire(new PostSummon(this.ika));
    }

    @Override
    public @Nullable Memosprite<Ika, Hyacine> getMemo() {
        return this.ika;
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent e) {
        getBattle().registerForPlayers(c -> {
            c.addPower(new SafeguardTheCandleInTheNight());
        });
    }

    @Override
    protected void useBasic() {
        this.doAttack(DamageType.BASIC, dl -> {
            dl.logic(this.getTarget(MoveType.BASIC), (e, al) -> {
                al.hit(e, 0.5f, MultiplierStat.HP, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            });
        });
    }

    @Override
    protected void useSkill() {
        this.summonMemo();

        getBattle().getPlayers().forEach(p -> {
            if (p == this.ika) {
                p.increaseHealth(this, 0.1f * this.getFinalHP()+200);
            } else {
                p.increaseHealth(this, 0.08f * this.getFinalHP() + 160);
            }
        });
        this.GentleThunderstorm();
    }

    @Override
    protected void useUltimate() {
        this.summonMemo();
        this.PetrichoricClearSkies.set(3);

        getBattle().getPlayers().forEach(p -> {
            if (p == this.ika) {
                p.increaseHealth(this, 0.12f * this.getFinalHP() + 240);
            } else {
                p.increaseHealth(this, 0.1f * this.getFinalHP() + 200);
            }
        });
        this.GentleThunderstorm();
    }

    @Subscribe
    public void onTurnStart(TurnStartEvent e) {
        this.PetrichoricClearSkies.decrease(1, 0);

        if (this.PetrichoricClearSkies.get() > 0) {
            return;
        }

        getBattle().getPlayers().forEach(p -> p.removePower(WeWhoFlyIntoTwilight.NAME));
    }

    @Subscribe
    public void ikaActions(TurnEndEvent e) {
        if (this.PetrichoricClearSkies.get() == 0) {
            return;
        }

        if (this.ika == null) {
            return;
        }

        this.ika.takeTurn();
    }

    @Subscribe
    public void onIkaDeath(MemospriteDeath e) {
        getBattle().AdvanceEntity(this, 30);
    }

    public void GentleThunderstorm() {
        getBattle().getPlayers().forEach(p -> {
            var debuff = p.powerList.stream()
                    .filter(power -> power.type == AbstractPower.PowerType.DEBUFF)
                    .findFirst();

            debuff.ifPresent(p::removePower);
        });
    }

    public final class TempestuousHalt extends PermPower {
        public TempestuousHalt() {
            super("Tempestuous Halt");

            this.setConditionalStat(PowerStat.HP_PERCENT, c -> Hyacine.this.getFinalSpeed() >= 200 ? 20f : 0f);
            this.setConditionalStat(PowerStat.OUTGOING_HEALING, c -> {
                if (Hyacine.this.getFinalSpeed() <= 200) {
                    return 0f;
                }

                var exceed = Math.floor(c.getFinalSpeed() - 200);
                return (float) Math.max(exceed, 200);
            });
        }
    }

    public static final class CloudyGrin extends PermPower {
        public CloudyGrin() {
            super("Cloudy Grin");

            this.setConditionalStat(PowerStat.OUTGOING_HEALING, c -> {
                if (c.getCurrentHp().get() <= c.getFinalHP() * 0.5f) {
                    return 25f;
                }
                return  0f;
            });
        }

        @Override
        public float setFixedCritRate(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes, float currentCrit) {
            return 100;
        }
    }


    public final class WeWhoFlyIntoTwilight extends PermPower {

        public static final String NAME = "We Who Fly Into Twilight";

        public WeWhoFlyIntoTwilight() {
            super(NAME);

            this.setStat(PowerStat.HP_PERCENT, eidolon.isActivated(Eidolon.E1) ? 80 : 30);
            this.setStat(PowerStat.FLAT_HP, 300);
        }
    }

    public final class SafeguardTheCandleInTheNight extends PermPower {

        @Subscribe
        public void afterAttack(PostAllyAttack e) {
            if (!eidolon.isActivated(Eidolon.E1)) {
                return;
            }

            ((AbstractCharacter<?>)this.getOwner())
                    .increaseHealth(Hyacine.this, 0.08f * Hyacine.this.getFinalHP());
        }
    }
}
