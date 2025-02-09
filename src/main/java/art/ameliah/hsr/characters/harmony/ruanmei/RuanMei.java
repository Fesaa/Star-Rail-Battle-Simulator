package art.ameliah.hsr.characters.harmony.ruanmei;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.turn.SkillCounterTurnGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
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

    public void useSkill() {
        skillCounter = 3;
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.addPower(skillPower);
        }
    }

    public void useBasic() {
        this.doAttack(DamageType.BASIC,
                dh -> dh.logic(this.getTarget(MoveType.BASIC),
                        (enemy, al) -> al.hit(enemy, 1, TOUGHNESS_DAMAGE_SINGLE_UNIT)));
    }

    public void useUltimate() {
        ultCounter = 2;
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.addPower(ultPower);
        }
    }

    public void onTurnStart() {
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

    public void onCombatStart() {
        getBattle().registerForPlayers(p -> {
            p.addPower(PermPower.create(PowerStat.BREAK_EFFECT, 20, "Ruan Mei Break Buff"));
            if (p != this) {
                p.addPower(PermPower.create(PowerStat.SPEED_PERCENT, 10, "Ruan Mei Speed Buff"));
            }
        });
    }

    public void onWeaknessBreak(AbstractEnemy enemy) {
        // TODO: ADD BREAK DMG
        //getBattle().getHelper().breakDamageHitEnemy(this, enemy, 1.2f);
    }

    public void useTechnique() {
        this.skillSequence();
        getBattle().generateSkillPoint(this, 1);
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

    private class RuanMeiUltPower extends PermPower {
        public RuanMeiUltPower() {
            super(ULT_POWER_NAME);
            this.setStat(PowerStat.RES_PEN, 25);
        }

        @Override
        public void beforeAttack(AttackLogic attack) {
            for (AbstractEnemy enemy : attack.getTargets()) {
                if (!enemy.hasPower(ULT_DEBUFF_NAME)) {
                    AbstractPower debuff = new RuanMeiUltDebuff(RuanMei.this);
                    enemy.addPower(debuff);
                }
            }
        }
    }
}
