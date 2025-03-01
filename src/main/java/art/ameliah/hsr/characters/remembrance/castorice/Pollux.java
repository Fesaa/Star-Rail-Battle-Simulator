package art.ameliah.hsr.characters.remembrance.castorice;

import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.battleLogic.log.lines.character.DoMove;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.remembrance.Memosprite;
import art.ameliah.hsr.events.EventPriority;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.HPLost;
import art.ameliah.hsr.events.character.PostSkill;
import art.ameliah.hsr.events.character.PreSkill;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.events.combat.DeathEvent;
import art.ameliah.hsr.events.combat.TurnEndEvent;
import art.ameliah.hsr.powers.PermPower;

public class Pollux extends Memosprite<Pollux, Castorice> {

    public final static String NAME = "Pollux";

    private int actionCounter = 0;

    public Pollux(Castorice master) {
        super(master, NAME, (int) Castorice.MAX_STAMEN_NOVA, 140, 80, ElementType.QUANTUM,
                0, 100, Path.REMEMBRANCE);

        this.usesEnergy = false;

        // Normal Memosprites always basic, Pollux is different.
        this.clearTurnGoals();
        // TODO: Make Pollux goals
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
        this.actionMetric.record(MoveType.SKILL);
        getBattle().addToLog(new DoMove(this, MoveType.MEMOSPRITE_SKILL));

        this.DimscorchBreath(this.actionCounter);
    }

    @Subscribe(priority = EventPriority.HIGHEST)
    public void onTurnEnd(TurnEndEvent e) {
        this.actionCounter = 0;
    }

    @Override
    protected void memoSkill() {
        // Not used
    }

    private void RendTheRealmBeneath() {
        this.doAttack(DamageType.MEMOSPRITE_DAMAGE, dl -> {
            dl.logic(getBattle().getEnemies(), (e, al) -> {
                al.hit(this.getMaster(), e, 0.3f, MultiplierStat.HP, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            });
        });
    }

    private void DimscorchBreath(float mul) {
        float dmgMul = 30f + mul * 4;
        this.startAttack().handle(DamageType.MEMOSPRITE_DAMAGE, dl -> {
            this.reduceHealth(this, Castorice.MAX_STAMEN_NOVA * 0.25f, this.getCurrentHp().get() != 1);

            dl.logic(getBattle().getEnemies(), (e, al) -> {
                al.hit(this.getMaster(), e, 0.3f, MultiplierStat.HP, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            });
        }).afterAttackHook(() -> {
            if (this.currentHp.get() > 0) {
                this.actionCounter++;
                this.doAction(); // Skill does not end action
            }
        }).execute();
    }

    @Subscribe
    public void onDeath(DeathEvent event) {
        // Where The West Wind Dwells
        getBattle().getPlayers().forEach(p -> {
            p.increaseHealth(this, 250 + 0.1 * this.getMaster().getFinalHP());
        });
        // Ebon Wings Over Scorched Ruins
        this.doAttack(DamageType.MEMOSPRITE_DAMAGE, dl -> {
            for (int i = 0; i < 6; i++) {
                dl.logic(getBattle().getRandomEnemy(),
                        (e, al) -> al.hit(this.getMaster(), e,
                                0.5f, MultiplierStat.HP, 4));
            }
        });
    }
}
