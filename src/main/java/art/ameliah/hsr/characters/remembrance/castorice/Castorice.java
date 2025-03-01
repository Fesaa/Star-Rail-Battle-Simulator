package art.ameliah.hsr.characters.remembrance.castorice;

import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.battleLogic.log.lines.character.DoMove;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.remembrance.Memomaster;
import art.ameliah.hsr.characters.remembrance.Memosprite;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.HPLost;
import art.ameliah.hsr.events.character.HpGain;
import art.ameliah.hsr.events.character.PostSkill;
import art.ameliah.hsr.events.character.PostSummon;
import art.ameliah.hsr.events.character.PreSkill;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.metrics.BoolMetric;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;
import org.jetbrains.annotations.Nullable;

public class Castorice extends Memomaster<Castorice> {

    public final static String NAME = "Castorice";
    public final static float MAX_STAMEN_NOVA = 80*4*100f;

    public final BoolMetric LostNetherland = metricRegistry.register("castorice::lost-netherland", BoolMetric.class);

    @Nullable
    private Pollux pollux;

    public Castorice() {
        super(NAME, 1629, 523, 485, 95, 80, ElementType.QUANTUM,
                MAX_STAMEN_NOVA, 100, Path.REMEMBRANCE);

        this.usesEnergy = false;
        this.currentEnergy.set(0f);
        this.addPower(new TracePower()
                .setStat(PowerStat.CRIT_CHANCE, 18.6f)
                .setStat(PowerStat.QUANTUM_DMG_BOOST, 14.4f)
                .setStat(PowerStat.CRIT_DAMAGE, 13.3f));

        this.addPower(new DarkTideContainedListener());
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        getBattle().registerForPlayers(p -> {
            p.addPower(new InvertedTorch());
            p.addPower(new DarkTideContainedListener());
            p.addPower(new DesolationTraversesHerPalmsListener());
        });

        getBattle().registerForEnemy(e -> {
            e.addPower(new LostNetherland());
        });
    }

    @Override
    protected void summonMemo() {
        this.pollux = new Pollux(this);
        int idx = getBattle().getPlayers().indexOf(this);
        getBattle().addPlayerAt(this.pollux, idx+1);

        this.eventBus.fire(new PostSummon(this.pollux));
        getBattle().getPlayers().forEach(p -> p.addPower(new DesolationBrokenByBellows()));
    }

    @Override
    public @Nullable Memosprite<?, ?> getMemo() {
        return this.pollux;
    }

    @Override
    public void useTechnique() {
        getBattle().setUsedEntryTechnique(true);
        this.currentEnergy.set(0.3f * MAX_STAMEN_NOVA);

        this.summonMemo();
        if (this.pollux == null) {
            throw new IllegalStateException();
        }

        this.pollux.getCurrentHp().set(this.pollux.getFinalHP() * 0.5f);
        getBattle().getPlayers().forEach(p -> {
            if (p != this.pollux)
                p.reduceHealth(this, p.getFinalHP() * 0.4f, false);
        });
        getBattle().AdvanceEntity(this.pollux, 100);
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
    protected void skillSequence() {
        if (this.pollux != null) {
            this.actionMetric.record(MoveType.ENHANCED_SKILL);
            getBattle().addToLog(new DoMove(this, MoveType.ENHANCED_SKILL));
            getBattle().useSkillPoint(this, 1);
            this.eventBus.fire(new PreSkill());
            this.useEnhancedSkill();
            this.eventBus.fire(new PostSkill());
            return;
        }

        super.skillSequence();
    }

    @Override
    protected void useSkill() {
        this.doAttack(DamageType.SKILL, dl -> {
            getBattle().getPlayers().forEach(p -> {
                p.reduceHealth(this, p.getCurrentHp().get() * 0.4f, false);
            });

            int idx = this.getTargetIdx(MoveType.SKILL);
            dl.logic(idx-1, (e, al) -> al.hit(e, 0.3f, MultiplierStat.HP, TOUGHNESS_DAMAGE_SINGLE_UNIT));
            dl.logic(idx, (e, al) -> al.hit(e, 0.5f, MultiplierStat.HP, TOUGHNESS_DAMAGE_TWO_UNITS));
            dl.logic(idx+1, (e, al) -> al.hit(e, 0.3f, MultiplierStat.HP, TOUGHNESS_DAMAGE_SINGLE_UNIT));
        });
    }

    protected void useEnhancedSkill() {
        if (this.pollux == null) {
            throw new IllegalStateException();
        }

        this.doAttack(DamageType.SKILL, dl -> {
            getBattle().getPlayers().forEach(p -> {
                p.reduceHealth(this, p.getCurrentHp().get() * 0.5f, false);
            });

            dl.logic(getBattle().getEnemies(), (e, al) -> {
                al.hit(e, 0.24f, MultiplierStat.HP, TOUGHNESS_DAMAGE_TWO_UNITS);
            });

            this.pollux.doAttack(DamageType.MEMOSPRITE_DAMAGE, dl2 -> {
                dl2.logic(getBattle().getEnemies(), (e, al) -> {
                    al.hit(this, e, 0.42f, MultiplierStat.HP, 0);
                });
            });

        });
    }

    @Override
    protected void useUltimate() {

        this.summonMemo();
        if (this.pollux == null) {
            throw new IllegalStateException();
        }

        getBattle().AdvanceEntity(this.pollux, 100);
        this.LostNetherland.set(true);
    }

    public class LostNetherland extends PermPower {

        public final static String NAME = "Lost Netherland";

        public LostNetherland() {
            super(NAME);
            this.setConditionalStat(PowerStat.RES_DOWN,
                    _ -> Castorice.this.LostNetherland.value() ? 20f : 0f);
        }
    }

    public static class DesolationThatTraversesHerPalms extends TempPower {
        public final static String NAME = "Desolation That Traverses Her Palms";

        public DesolationThatTraversesHerPalms() {
            super(NAME);

            this.maxStacks = 3;
            this.setConditionalStat(PowerStat.DAMAGE_BONUS, _ -> this.stacks * 20f);
        }
    }

    public class DesolationTraversesHerPalmsListener extends PermPower {
        @Subscribe
        public void afterHPLost(HPLost e) {
            if (this.getOwner() == Castorice.this.pollux) {
                return;
            }

            Castorice.this.addPower(new DesolationThatTraversesHerPalms());
            if (Castorice.this.pollux == null) {
                Castorice.this.currentEnergy.increase(e.getAmount(), MAX_STAMEN_NOVA);
            } else {
                Castorice.this.pollux.addPower(new DesolationThatTraversesHerPalms());
                Castorice.this.pollux.increaseHealth(this, e.getAmount(), false);
            }
        }
    }

    public class InvertedTorch extends PermPower {
        @Subscribe
        public void afterHpGain(HpGain e) {
            var increase = Math.min(e.getOverflow(), 0.15f * MAX_STAMEN_NOVA);

            Pollux pollux = (Pollux) Castorice.this.getMemo();
            if (pollux == null) {
                Castorice.this.currentEnergy.increase(increase, MAX_STAMEN_NOVA);
            } else if (this.getOwner() != pollux){
                pollux.increaseHealth(this, increase);
            }
        }
    }

    public static class DarkTideContainedListener extends PermPower {
        @Subscribe
        public void afterHpGain(HpGain e) {
            AbstractCharacter<?> owner = (AbstractCharacter<?>) this.getOwner();
            if (owner.getCurrentHp().get() < owner.getFinalHP() * 0.5f) {
                return;
            }

            var power = owner.getPower(DarkTideContained.NAME);
            if (power == null) {
                getBattle().IncreaseSpeed(owner, new DarkTideContained());
            }
        }

        @Subscribe
        public void afterHPLost(HPLost e) {
            AbstractCharacter<?> owner = (AbstractCharacter<?>) this.getOwner();

            if (owner.getCurrentHp().get() >= owner.getFinalHP() * 0.5f) {
                return;
            }

            var power = owner.getPower(DarkTideContained.NAME);
            if (power != null) {
                getBattle().DecreaseSpeed(owner, power);
            }
        }
    }

    public static class DarkTideContained extends PermPower {

        public final static String NAME = "Dark Tide Contained";

        public DarkTideContained() {
            super(NAME);
            this.setStat(PowerStat.SPEED_PERCENT, 40);
        }
    }

    public static class DesolationBrokenByBellows extends TempPower {
        private DesolationBrokenByBellows() {
            super(2, "Desolation Broken By Bellows");

            this.setStat(PowerStat.DAMAGE_BONUS, 10);
        }
    }
}
