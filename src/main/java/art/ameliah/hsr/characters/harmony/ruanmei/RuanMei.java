package art.ameliah.hsr.characters.harmony.ruanmei;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.turn.SkillCounterTurnGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreAllyAttack;
import art.ameliah.hsr.events.combat.AllyJoinCombat;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.events.combat.TurnStartEvent;
import art.ameliah.hsr.events.enemy.WeaknessBreakEvent;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TracePower;

public class RuanMei extends AbstractCharacter<RuanMei> implements SkillCounterTurnGoal.SkillCounterCharacter {
    public static final String NAME = "Ruan Mei";
    public static final String SKILL_POWER_NAME = "RuanMeiSkillPower";
    public static final String ULT_POWER_NAME = "RuanMeiUltPower";
    public static final String ULT_DEBUFF_NAME = "RuanMeiUltDebuff";
    final PermPower skillPower;
    final AbstractPower ultPower = new RuanMeiUltPower();
    private int skillCounter = 0;
    private int ultCounter = 0;

    public RuanMei() {
        super(NAME, 1087, 660, 485, 104, 80, ElementType.ICE, 130, 100, Path.HARMONY);

        this.addPower(new TracePower()
                .setStat(PowerStat.BREAK_EFFECT, 37.3f)
                .setStat(PowerStat.DEF_PERCENT, 22.5f)
                .setStat(PowerStat.FLAT_SPEED, 5));

        this.skillPower = (PermPower) new PermPower(SKILL_POWER_NAME)
                .setStat(PowerStat.DAMAGE_BONUS, 68)
                .setStat(PowerStat.WEAKNESS_BREAK_EFF, 50);

        this.registerGoal(0, new AlwaysUltGoal<>(this));
        this.registerGoal(0, new SkillCounterTurnGoal<>(this));
        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));
    }

    @Override
    protected boolean skillConsumesSP() {
        return this.getTurns() > 0;
    }

    @Subscribe
    public void onAllyJoinCombat(AllyJoinCombat e) {
        if (this.skillCounter > 0 || !e.getAlly().hasPower(SKILL_POWER_NAME)) {
            e.getAlly().addPower(skillPower);
        }
    }

    @Override
    public void useSkill() {
        skillCounter = 3;
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.addPower(skillPower);
        }
    }

    @Override
    public void useBasic() {
        this.doAttack(DamageType.BASIC,
                dh -> dh.logic(this.getTarget(MoveType.BASIC),
                        (enemy, al) -> al.hit(enemy, 1, TOUGHNESS_DAMAGE_SINGLE_UNIT)));
    }

    @Override
    public void useUltimate() {
        ultCounter = 2;
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.addPower(ultPower);
        }
    }

    @Subscribe
    public void onTurnStart(TurnStartEvent e) {
        increaseEnergy(5, TRACE_ENERGY_GAIN);
        if (skillCounter > 0) {
            skillCounter--;
            if (skillCounter <= 0) {
                for (AbstractCharacter<?> character : getBattle().getPlayers()) {
                    character.removePower(skillPower);
                }
            }
        }
        if (ultCounter > 0) {
            ultCounter--;
            if (ultCounter <= 0) {
                for (AbstractCharacter<?> character : getBattle().getPlayers()) {
                    character.removePower(ultPower);
                }
            }
        }
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent e) {
        getBattle().registerForPlayers(p -> {
            p.addPower(PermPower.create(PowerStat.BREAK_EFFECT, 20, "Ruan Mei Break Buff"));
            if (p != this) {
                getBattle().IncreaseSpeed(p, PermPower.create(PowerStat.SPEED_PERCENT, 10, "Ruan Mei Speed Buff"));
            }
        });
    }

    @Subscribe
    public void onWeaknessBreak(WeaknessBreakEvent e) {
        // TODO: ADD BREAK DMG
        //getBattle().getHelper().breakDamageHitEnemy(this, enemy, 1.2f);
    }

    public void useTechnique() {
        this.skillSequence();
    }

    @Override
    public int getSkillCounter() {
        return skillCounter;
    }

    public static class RuanMeiUltDebuff extends AbstractPower {

        public final AbstractCharacter<?> owner;
        public boolean triggered = false;

        public RuanMeiUltDebuff(AbstractCharacter<?> owner) {
            this.setName(ULT_DEBUFF_NAME);
            this.lastsForever = true;
            this.type = PowerType.DEBUFF;
            this.owner = owner;
        }
    }

    public class RuanMeiUltPower extends PermPower {
        public RuanMeiUltPower() {
            super(ULT_POWER_NAME);
            this.setStat(PowerStat.RES_PEN, 25);
        }

        @Subscribe
        public void beforeAttack(PreAllyAttack e) {
            for (AbstractEnemy enemy : e.getAttack().getTargets()) {
                if (!enemy.hasPower(ULT_DEBUFF_NAME)) {
                    AbstractPower debuff = new RuanMeiUltDebuff(RuanMei.this);
                    enemy.addPower(debuff);
                }
            }
        }
    }
}
