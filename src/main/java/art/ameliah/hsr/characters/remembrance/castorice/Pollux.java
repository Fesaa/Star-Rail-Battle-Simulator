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
import art.ameliah.hsr.metrics.Metric;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

import java.util.List;


public class Pollux extends Memosprite<Pollux, Castorice> {

    public final static String NAME = "Pollux";

    public boolean shouldDie = false;
    private int actionCounter = 0;
    private int breathScorchesTheShadowCounter = 0;

    public Pollux(Castorice master, ActionMetric actionMetric, CounterMetric<Integer> turnsMetric) {
        super(master, NAME, (int) Castorice.MAX_NEWBUD, 165, 80, ElementType.QUANTUM,
                0, 100, Path.REMEMBRANCE);

        this.usesEnergy = false;

        if (actionMetric != null) {
            this.actionMetric = this.metricRegistry.register(actionMetric);
        }
        if (turnsMetric != null) {
            this.turnsMetric = this.metricRegistry.register(turnsMetric);
        }


        // Normal Memosprites always basic, Pollux is different.
        this.clearTurnGoals();
        // TODO: Make Pollux goals
        this.registerGoal(0, new SkillOnBattleEnd(this));
        this.registerGoal(100, new PolluxSkillGoal(this));
        //this.registerGoal(0, new AlwaysSkillGoal<>(this));
    }

    @Override
    public float getFinalHP() {
        return Castorice.MAX_NEWBUD;
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

        this.BreathScorchesTheShadow();
    }

    @Subscribe(priority = EventPriority.HIGHEST)
    public void onTurnEnd(TurnEndEvent e) {
        if (this.getTurns() % 3 == 0 && this.currentHp.get() > 0 && !this.shouldDie) {
            this.die(this);
        }
        this.shouldDie = false;
        this.actionCounter = 0;
    }

    @Override
    protected void memoSkill() {
        // Not used
    }

    private void RendTheRealmBeneath() {
        this.doAttack(DamageType.MEMOSPRITE_DAMAGE, dl -> {
            dl.logic(getBattle().getEnemies(), (e, al) -> {
                al.setMultiSource(this.getMaster());
                al.hit(e, 0.4f, MultiplierStat.HP, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            });
        });
    }

    private void BreathScorchesTheShadow() {
        var whereTheWestWindDwells = PermPower.create(PowerStat.QUANTUM_DMG_BOOST, 0.3f*Math.min(6, this.actionCounter), "Where The West Wind Dwells");
        this.addPower(whereTheWestWindDwells);
        float dmgMul = 0.24f + Math.min(this.breathScorchesTheShadowCounter, 3) * 0.04f;
        this.startAttack().handle(DamageType.MEMOSPRITE_DAMAGE, dl -> {
            this.reduceHealth(this, Castorice.MAX_NEWBUD * 0.25f, false);
            this.shouldDie = this.currentHp.get() == 1;

            dl.logic(getBattle().getEnemies(), (e, al) -> {
                al.setMultiSource(this.getMaster());
                al.hit(e, dmgMul, MultiplierStat.HP, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            });
        }).afterAttackHook(() -> {
            if (getBattle().enemiesSize() == 0) {
                getBattle().IncreaseSpeed(this, TempPower.create(PowerStat.SPEED_PERCENT, 100, 1, "Inverted Torch"));
            }

            this.removePower(whereTheWestWindDwells);
            if (this.currentHp.get() > 1 && !this.shouldDie) {
                this.actionCounter++;
                this.breathScorchesTheShadowCounter++;
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
        this.getMaster().setPolluxActionMetric(this.actionMetric);
        this.getMaster().setPolluxTurnsMetric(this.turnsMetric);
    }


}
