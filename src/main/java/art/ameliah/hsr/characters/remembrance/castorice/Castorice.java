package art.ameliah.hsr.characters.remembrance.castorice;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.battleLogic.combat.hit.EnemyHit;
import art.ameliah.hsr.battleLogic.log.lines.character.DoMove;
import art.ameliah.hsr.battleLogic.log.lines.character.GainEnergy;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.UltGoalResult;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysSkillGoal;
import art.ameliah.hsr.characters.remembrance.Memomaster;
import art.ameliah.hsr.characters.remembrance.Memosprite;
import art.ameliah.hsr.characters.remembrance.trailblazer.Mem;
import art.ameliah.hsr.events.EventPriority;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.HPGain;
import art.ameliah.hsr.events.character.HPLost;
import art.ameliah.hsr.events.character.MemospriteDeath;
import art.ameliah.hsr.events.character.PostSkill;
import art.ameliah.hsr.events.character.PostSummon;
import art.ameliah.hsr.events.character.PreSkill;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.events.combat.DeathEvent;
import art.ameliah.hsr.events.combat.TurnStartEvent;
import art.ameliah.hsr.metrics.ActionMetric;
import art.ameliah.hsr.metrics.CounterMetric;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Castorice extends Memomaster<Castorice> {

    public final static String NAME = "Castorice";
    public final static float MAX_NEWBUD = 32000;

    @Nullable
    private Pollux pollux;
    @Getter
    @Setter
    private ActionMetric polluxActionMetric;
    @Getter
    @Setter
    private CounterMetric<Integer> polluxTurnsMetric;

    public Castorice() {
        super(NAME, 1629, 523, 485, 95, 80, ElementType.QUANTUM,
                MAX_NEWBUD, 100, Path.REMEMBRANCE);

        this.isDPS = true;
        this.usesEnergy = false;
        this.currentEnergy.set(0f);
        this.addPower(new TracePower()
                .setStat(PowerStat.CRIT_CHANCE, 18.6f)
                .setStat(PowerStat.QUANTUM_DMG_BOOST, 14.4f)
                .setStat(PowerStat.CRIT_DAMAGE, 13.3f));

        // Just using the most simply goals for now
        this.registerGoal(0, new AlwaysSkillGoal<>(this, -1));
        this.registerGoal(0, () -> this.pollux == null ? UltGoalResult.DO : UltGoalResult.DONT);
        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {

        this.addPower(new InvertedTorch());
        this.addPower(new InvertedTorchListener());

        getBattle().registerForPlayers(p -> {
            p.addPower(new DarkTideContained());
            p.addPower(new DesolationTraversesHerPalmsListener());
            p.addPower(new SanctuaryOfMooncocoon());
            p.addPower(new MoonShelledVessel());
        });
    }

    @Override
    protected void summonMemo() {
        this.pollux = new Pollux(this, this.polluxActionMetric, this.polluxTurnsMetric);

        int idx = getBattle().getPlayers().indexOf(this);
        getBattle().addPlayerAt(this.pollux, idx + 1);

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
        this.currentEnergy.set(0.3f * MAX_NEWBUD);

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
        getBattle().AdvanceEntity(this.pollux, 100); // Get pollux to 0 AV, the SPD buff is applied first
    }

    protected void increaseNewbud(float amount) {
        if (amount <= 0) {
            return;
        }

        float overflow = this.currentEnergy.increase(amount, MAX_NEWBUD);
        float gained = amount - overflow;

        getBattle().addToLog(new GainEnergy(Castorice.this,
                this.currentEnergy.get() - gained,
                this.currentEnergy.get(),
                gained, "Ally health changes"));

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
    protected boolean skillConsumesSP() {
        return false;
    }

    @Override
    protected void skillSequence() {
        if (this.pollux != null) {
            this.actionMetric.record(MoveType.ENHANCED_SKILL);
            getBattle().addToLog(new DoMove(this, MoveType.ENHANCED_SKILL));
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
                p.reduceHealth(this, p.getCurrentHp().get() * 0.3f, false);
            });

            int idx = this.getTargetIdx(MoveType.SKILL);
            dl.logic(idx - 1, (e, al) -> al.hit(e, 0.3f, MultiplierStat.HP, TOUGHNESS_DAMAGE_SINGLE_UNIT));
            dl.logic(idx, (e, al) -> al.hit(e, 0.5f, MultiplierStat.HP, TOUGHNESS_DAMAGE_TWO_UNITS));
            dl.logic(idx + 1, (e, al) -> al.hit(e, 0.3f, MultiplierStat.HP, TOUGHNESS_DAMAGE_SINGLE_UNIT));
        });
    }

    protected void useEnhancedSkill() {
        if (this.pollux == null) {
            throw new IllegalStateException();
        }

        this.doAttack(DamageType.SKILL, dl -> {
            getBattle().getPlayers().forEach(p -> {
                if (p == this.pollux) {
                    return;
                }
                p.reduceHealth(this, p.getCurrentHp().get() * 0.4f, false);
            });

            dl.logic(getBattle().getEnemies(), (e, al) -> {
                al.hit(e, 0.3f, MultiplierStat.HP, TOUGHNESS_DAMAGE_TWO_UNITS);
            });

            this.pollux.doAttack(DamageType.MEMOSPRITE_DAMAGE, dl2 -> {
                dl2.logic(getBattle().getEnemies(), (e, al) -> {
                    al.setMultiSource(this);
                    al.hit(e, 0.5f, MultiplierStat.HP, 0);
                });
            });

        });
    }

    @Override
    protected void useUltimate() {
        if (this.pollux != null) {
            throw new IllegalStateException();
        }

        this.summonMemo();
        if (this.pollux == null) {
            throw new IllegalStateException();
        }

        getBattle().AdvanceEntity(this.pollux, 100);
        getBattle().getEnemies().forEach(e -> e.addPower(new LostNetherland()));
    }

    @Subscribe
    public void onPolluxDeath(MemospriteDeath event) {
        this.pollux = null;
        getBattle().getEnemies().forEach(e -> e.removePower(LostNetherland.NAME));
    }

    public static class LostNetherland extends PermPower {

        public final static String NAME = "Lost Netherland";

        public LostNetherland() {
            super(NAME);
            this.setStat(PowerStat.RES_DOWN, 20f);
        }
    }

    public static class DesolationThatTraversesHerPalms extends TempPower {
        public final static String NAME = "Desolation That Traverses Her Palms";

        public DesolationThatTraversesHerPalms() {
            super(3, NAME);

            this.maxStacks = 3;
            this.setConditionalStat(PowerStat.DAMAGE_BONUS, _ -> this.stacks * 20f);
        }
    }

    public static class InvertedTorchListener extends PermPower {

        @Subscribe
        public void onCombatStart(CombatStartEvent e) {
            AbstractCharacter<?> owner = (AbstractCharacter<?>) this.getOwner();
            if (owner.getCurrentHp().get() < owner.getFinalHP() * 0.5f) {
                return;
            }

            getBattle().IncreaseSpeed(owner, new InvertedTorch());
        }

        @Subscribe
        public void afterHpGain(HPGain e) {
            AbstractCharacter<?> owner = (AbstractCharacter<?>) this.getOwner();
            if (owner.getCurrentHp().get() < owner.getFinalHP() * 0.5f) {
                return;
            }

            var power = owner.getPower(InvertedTorch.NAME);
            if (power == null) {
                getBattle().IncreaseSpeed(owner, new InvertedTorch());
            }
        }

        @Subscribe
        public void afterHPLost(HPLost e) {
            AbstractCharacter<?> owner = (AbstractCharacter<?>) this.getOwner();

            if (owner.getCurrentHp().get() >= owner.getFinalHP() * 0.5f) {
                return;
            }

            var power = owner.getPower(InvertedTorch.NAME);
            if (power != null) {
                getBattle().DecreaseSpeed(owner, power);
            }
        }
    }

    public static class InvertedTorch extends PermPower {

        public final static String NAME = "Inverted Torch";

        public InvertedTorch() {
            super(NAME);
            this.setStat(PowerStat.SPEED_PERCENT, 40);
        }
    }

    public static class DesolationBrokenByBellows extends TempPower {
        private DesolationBrokenByBellows() {
            super(3, "Desolation Broken By Bellows");

            this.setStat(PowerStat.DAMAGE_BONUS, 10);
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
                Castorice.this.increaseNewbud(e.getAmount());
            } else {
                Castorice.this.pollux.addPower(new DesolationThatTraversesHerPalms());
                Castorice.this.pollux.increaseHealth(this, e.getAmount(), false);
            }
        }

        @Subscribe
        public void onSummon(PostSummon e) {
            e.getMemosprite().addPower(new DesolationThatTraversesHerPalms());
        }
    }

    public class DarkTideContained extends PermPower {

        private static final float MAX = 0.12f * MAX_NEWBUD;

        private static final Map<String, Float> cumulatives = new HashMap<>(8);

        private float getIncrease(BattleParticipant source, float amount) {
            var cumulative = cumulatives.getOrDefault(source.getName(), 0f);
            if (cumulative >= MAX) {
                return 0;
            }

            float maxIncrease = MAX - cumulative;
            float actualIncrease = Math.min(maxIncrease, amount);
            cumulatives.put(source.getName(), cumulative+actualIncrease);
            return actualIncrease;
        }

        @Subscribe
        public void onTurnStart(TurnStartEvent e) {
            cumulatives.clear();
        }

        @Subscribe
        public void afterHpGain(HPGain e) {
            if (this.getOwner() == Castorice.this.pollux) {
                return;
            }

            var increase = this.getIncrease(this.getOwner(), e.getAmount()+e.getOverflow());

            Pollux pollux = (Pollux) Castorice.this.getMemo();
            if (pollux == null || pollux.getCurrentHp().get() <= 1) {
                Castorice.this.increaseNewbud(increase);
                return;
            }

            if (!pollux.shouldDie) {
                pollux.increaseHealth(this, increase);
            }
        }
    }

    public class SanctuaryOfMooncocoon extends PermPower {

        private static boolean hasTriggered = false;
        private boolean activated = false;
        private int actionCooldown = 1;

        @Subscribe
        public void onTurnStart(TurnStartEvent e) {
            if (!this.activated) {
                return;
            }

            if (this.actionCooldown > 0) {
                this.actionCooldown--;
                return;
            }

            this.activated = false;
            AbstractCharacter<?> owner = (AbstractCharacter<?>) this.getOwner();
            if (owner.getCurrentHp().get() > 1) {
                return;
            }

            owner.die(this);
        }

        @Subscribe(priority = EventPriority.HIGHEST)
        public void onDeath(DeathEvent event) {
            if (hasTriggered || this.getOwner() == Castorice.this.pollux) {
                return;
            }


            AbstractCharacter<?> owner = (AbstractCharacter<?>) this.getOwner();
            owner.getCurrentHp().set(1f);

            hasTriggered = true;
            this.activated = true;
            event.setCanceled(true);
        }
    }

    public class MoonShelledVessel extends PermPower {

        private static final String NAME = "Moon-Shelled Vessel";

        public MoonShelledVessel() {
            super(NAME);
        }

        @Subscribe(priority = EventPriority.LOWEST)
        public void onHPLost(HPLost event) {
            if (Castorice.this.pollux == null) {
                return;
            }

            if (this.owner == Castorice.this.pollux) {
                return;
            }

            AbstractCharacter<?> owner = (AbstractCharacter<?>) this.getOwner();
            if (owner.getCurrentHp().get() > event.getAmount()) {
                return;
            }

            var polluxDmg = (event.getAmount() - owner.getCurrentHp().get() + 1) * 5;
            var ownerDmg = owner.getCurrentHp().get() - 1;

            if (Castorice.this.pollux.getCurrentHp().get() < polluxDmg) {
                ownerDmg += (polluxDmg - Castorice.this.pollux.getCurrentHp().get()) / 5;
                polluxDmg = Castorice.this.pollux.getCurrentHp().get();
            }


            var hit = new EnemyHit(event.getSource(), owner, 0, polluxDmg, null);
            Castorice.this.pollux.hit(hit);
            event.setAmount(ownerDmg);
        }

    }
}
