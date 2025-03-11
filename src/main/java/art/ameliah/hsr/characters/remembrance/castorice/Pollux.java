package art.ameliah.hsr.characters.remembrance.castorice;

import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.battleLogic.log.lines.character.DoMove;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.TurnGoalResult;
import art.ameliah.hsr.characters.remembrance.Memosprite;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.EventPriority;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.events.combat.DeathEvent;
import art.ameliah.hsr.events.combat.TurnEndEvent;
import art.ameliah.hsr.metrics.ActionMetric;
import art.ameliah.hsr.metrics.CounterMetric;
import art.ameliah.hsr.powers.PermPower;

import java.util.List;

public class Pollux extends Memosprite<Pollux, Castorice> {

    public final static String NAME = "Pollux";

    private static ActionMetric actionMetricTracker = null;
    private static CounterMetric<Integer> turnsMetricTracker = null;

    public boolean shouldDie = false;
    private int actionCounter = 0;

    public Pollux(Castorice master) {
        super(master, NAME, (int) Castorice.MAX_NEWBUD, 165, 80, ElementType.QUANTUM,
                0, 100, Path.REMEMBRANCE);

        this.usesEnergy = false;

        if (Pollux.actionMetricTracker != null) {
            this.metricRegistry.register(Pollux.actionMetricTracker);
            this.actionMetric = Pollux.actionMetricTracker;
        }
        if (Pollux.turnsMetricTracker != null) {
            this.metricRegistry.register(Pollux.turnsMetricTracker);
            this.turnsMetric = Pollux.turnsMetricTracker;
        }


        // Normal Memosprites always basic, Pollux is different.
        this.clearTurnGoals();
        // TODO: Make Pollux goals
        this.registerGoal(100, new SkillOnBattleEnd(this));
        this.registerGoal(90,
                () -> getBattle().getActionValueUsed() == 0 ? TurnGoalResult.SKILL : TurnGoalResult.PASS);
        this.registerGoal(0, new PolluxSkillGoal(this));
        //this.registerGoal(0, new AlwaysSkillGoal<>(this));
    }

    @Subscribe
    public void onCombatEnter(CombatStartEvent e) {
        this.addPower(new WhereTheWestWindDwells());
    }

    // Pollux is behind(?) the players, and can't be attacked
    @Override
    public boolean invincible() {
        return true;
    }

    @Override
    protected void basicSequence() {
        this.actionMetric.record(MoveType.MEMOSPRITE_SKILL);
        getBattle().addToLog(new DoMove(this, MoveType.MEMOSPRITE_SKILL));

        this.RendTheRealmBeneath();
    }

    @Override
    protected void skillSequence() {
        this.actionMetric.record(MoveType.MEMOSPRITE_SKILL);
        getBattle().addToLog(new DoMove(this, MoveType.MEMOSPRITE_SKILL));

        this.BreathScorchesTheShadow(this.actionCounter);
    }

    @Subscribe(priority = EventPriority.HIGHEST)
    public void onTurnEnd(TurnEndEvent e) {
        if (this.getTurns() % 3 == 0 && this.currentHp.get() > 0 && !this.shouldDie) {
            this.die(this);
        }
        this.shouldDie = false;
    }

    @Override
    protected void memoSkill() {
        // Not used
    }

    private void RendTheRealmBeneath() {
        this.doAttack(DamageType.MEMOSPRITE_DAMAGE, dl -> {
            dl.logic(getBattle().getEnemies(), (e, al) -> {
                al.setMultiSource(this.getMaster());
                al.hit(e, 0.24f, MultiplierStat.HP, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            });
        });
    }

    private void BreathScorchesTheShadow(float mul) {
        float dmgMul = 0.24f + mul * 0.04f;
        this.startAttack().handle(DamageType.MEMOSPRITE_DAMAGE, dl -> {
            this.reduceHealth(this, Castorice.MAX_NEWBUD * 0.25f, false);
            this.shouldDie = this.currentHp.get() == 1;

            dl.logic(getBattle().getEnemies(), (e, al) -> {
                al.setMultiSource(this.getMaster());
                al.hit(e, dmgMul, MultiplierStat.HP, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            });
        }).afterAttackHook(() -> {
            if (this.currentHp.get() > 1 && !this.shouldDie) {
                this.actionCounter = Math.min(this.actionCounter+1, 3);
                this.doAction(); // Skill does not end action
                return;
            }

            this.die(this);
        }).execute();
    }

    @Subscribe
    public void onDeath(DeathEvent event) {
        // Ebon Wings Over Scorched Ruins
        this.doAttack(DamageType.MEMOSPRITE_DAMAGE, dl -> {
            for (int i = 0; i < 6; i++) {
                dl.logic(getBattle().getRandomEnemy(),
                        (e, al) -> {
                            al.setMultiSource(this.getMaster());
                            al.hit(e, 0.4f, MultiplierStat.HP, 4);
                        });
            }
        });
        getBattle().getPlayers().forEach(p -> {
            if (this == p) {
                return;
            }
            p.increaseHealth(this, 800 + 0.06 * this.getMaster().getFinalHP());
        });
        Pollux.actionMetricTracker = this.actionMetric;
        Pollux.turnsMetricTracker = this.turnsMetric;
    }

    public static class WhereTheWestWindDwells extends PermPower {
        private static final String NAME = "Where The West Wind Dwells";

        WhereTheWestWindDwells() {
            super(NAME);

        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            float hpRel = enemy.getCurrentHp().get() / enemy.maxHp() * 100;
            return (1 - (hpRel-30) / 70) * 40;
        }

    }


}
