package art.ameliah.hsr.characters.fuxuan;

import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.characters.goal.shared.turn.SkillCounterTurnGoal;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class FuXuan extends AbstractCharacter<FuXuan> implements SkillCounterTurnGoal.SkillCounterCharacter {
    public static final String NAME = "Fu Xuan";

    final AbstractPower skillPower = PermPower.create(PowerStat.CRIT_CHANCE, 12, "Fu Xuan Skill Power");
    int skillCounter = 0;

    public FuXuan() {
        super(NAME, 1475, 466, 606, 100, 80, ElementType.QUANTUM, 135, 150, Path.PRESERVATION);

        PermPower tracesPower = new PermPower();
        tracesPower.setName("Traces Stat Bonus");
        tracesPower.setStat(PowerStat.HP_PERCENT, 18);
        tracesPower.setStat(PowerStat.CRIT_CHANCE, 18.7f);
        tracesPower.setStat(PowerStat.EFFECT_RES, 10);
        this.addPower(tracesPower);
        this.hasAttackingUltimate = true;

        this.registerGoal(0, new AlwaysUltGoal<>(this));
        this.registerGoal(0, new SkillCounterTurnGoal<>(this, 1));
        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));
    }

    public void useSkill() {
        if (skillCounter >= 1) {
            increaseEnergy(20, TRACE_ENERGY_GAIN);
        }
        skillCounter = 3;
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            if (!character.hasPower(skillPower.getName())) {
                character.addPower(skillPower);
            }
        }
    }

    public void useBasic() {
        skillCounter--;
        if (skillCounter <= 0) {
            for (AbstractCharacter<?> character : getBattle().getPlayers()) {
                character.removePower(skillPower);
            }
        }
        this.doAttack(DamageType.BASIC,
                dh -> dh.logic(this.getTarget(MoveType.BASIC),
                        (enemy, al) -> al.hit(enemy, 0.5f, MultiplierStat.HP, TOUGHNESS_DAMAGE_SINGLE_UNIT)));
    }

    public void useUltimate() {
        this.doAttack(DamageType.ULTIMATE, dh -> dh.logic(getBattle().getEnemies(),
                (enemies, al) -> al.hit(enemies, 1, MultiplierStat.HP, TOUGHNESS_DAMAGE_TWO_UNITS)));
    }

    public void useTechnique() {
        skillCounter = 2;
        skillPower.setStat(PowerStat.FLAT_HP, 0.06f * FuXuan.this.getFinalHP());
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.addPower(skillPower);
        }
    }

    @Override
    public int getSkillCounter() {
        return skillCounter;
    }
}
