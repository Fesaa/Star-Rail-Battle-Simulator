package art.ameliah.hsr.characters.sunday;

import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.AbstractSummoner;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.ally.DpsAllyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysSkillGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;

import java.util.Optional;

public class Sunday extends AbstractCharacter<Sunday> {

    public static final String NAME = "Sunday";
    public static final String SKILL_POWER_NAME = "SundaySkillPower";
    public static final String TECHNIQUE_POWER_NAME = "SundayTechniquePower";
    public static final String TheBeatified = "The Beatified";

    private boolean firstAbilityUse = true;
    private int theBeatifiedTurnsRemaining = 0;

    public Sunday() {
        super(NAME, 1242, 640, 534, 96, 80, ElementType.IMAGINARY, 130, 100, Path.HARMONY);

        this.addPower(new TracePower()
                .setStat(PowerStat.CRIT_DAMAGE, 37.7f)
                .setStat(PowerStat.EFFECT_RES, 18)
                .setStat(PowerStat.DEF_PERCENT, 12.5f)
        );

        this.registerGoal(0, new AlwaysSkillGoal<>(this));
        this.registerGoal(0, new AlwaysUltGoal<>(this));
        this.registerGoal(0, new SummonerTargetGoal(this));
        this.registerGoal(10, new DpsAllyTargetGoal<>(this));
        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));
    }

    @Override
    protected void useSkill() {
        var target = this.getAllyTarget();

        if (firstAbilityUse) {
            this.firstAbilityUse = false;
            target.addPower(TempPower.create(PowerStat.DAMAGE_BONUS, 50, 2, TECHNIQUE_POWER_NAME));
        }

        target.addPower(new SundaySkillPower());
        // TODO: Dispel debug

        if (target.hasPower(TheBeatified)) {
            getBattle().generateSkillPoint(this, 1);
        }

        if (target.getPath().equals(Path.HARMONY)) {
            return;
        }

        getBattle().AdvanceEntity(target, 100);
        if (target instanceof AbstractSummoner<?> summoner) {
            summoner.getSummons().forEach(s -> {
                getBattle().AdvanceEntity(s, 100);
            });
        }
    }

    @Override
    protected void useBasic() {
        this.startAttack()
                .hitEnemy(this.getTarget(MoveType.BASIC), 1, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.BASIC)
                .execute();
    }

    @Override
    protected void useUltimate() {
        AbstractCharacter<?> target = this.getAllyTarget();
        if (firstAbilityUse) {
            this.firstAbilityUse = false;
            target.addPower(TempPower.create(PowerStat.DAMAGE_BONUS, 50, 2, TECHNIQUE_POWER_NAME));
        }

        target.increaseEnergy(this.ultimateEnergyCharge(target), "Sunday Ultimate Energy increase");

        getBattle().getPlayers().forEach(p -> p.removePower(Sunday.TheBeatified));
        target.addPower(new TheBeatified());
        this.theBeatifiedTurnsRemaining = 3;
    }

    private float ultimateEnergyCharge(AbstractCharacter<?> target) {
        float per = target.maxEnergy * 0.2f;

        return Math.max(per, 40);
    }

    @Override
    public void onTurnStart() {
        this.theBeatifiedTurnsRemaining--;

        if (this.theBeatifiedTurnsRemaining <= 0) {
            getBattle().getPlayers().forEach(p -> p.removePower(Sunday.TheBeatified));
        }
    }

    public static class SundaySkillPower extends TempPower {

        public SundaySkillPower() {
            super(2, SKILL_POWER_NAME);

            float dmgBoost = this.getOwner() instanceof AbstractSummoner<?> ? 80 : 30;

            this.setStat(PowerStat.DAMAGE_BONUS, dmgBoost);
            this.setStat(PowerStat.CRIT_CHANCE, 20);
        }

    }


    public class TheBeatified extends PermPower {
        public TheBeatified() {
            super(TheBeatified);
            float cdBoost = 12 + 0.3f * Sunday.this.getTotalCritDamage();
            this.setStat(PowerStat.CRIT_DAMAGE, cdBoost);
        }
    }
}
