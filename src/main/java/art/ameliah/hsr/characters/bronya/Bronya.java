package art.ameliah.hsr.characters.bronya;

import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysSkillGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;

import java.util.List;

public class Bronya extends AbstractCharacter<Bronya> {

    public static final String NAME = "Bronya";
    public static final String SKILL_POWER_NAME = "BronyaSkillPower";
    public static final String ULT_POWER_NAME = "BronyaUltPower";

    public Bronya() {
        super(NAME, 1242, 582, 534, 99, 80, ElementType.WIND, 120, 100, Path.HARMONY);

        this.addPower(new TracePower()
                .setStat(PowerStat.WIND_DMG_BOOST, 22.4f)
                .setStat(PowerStat.CRIT_DAMAGE, 24)
                .setStat(PowerStat.EFFECT_RES, 10));

        this.registerGoal(0, new AlwaysUltGoal<>(this));
        this.registerGoal(0, new AlwaysSkillGoal<>(this));
        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));
    }

    public void useSkill() {
        AbstractPower skillPower = TempPower.create(PowerStat.DAMAGE_BONUS, 66, 1, SKILL_POWER_NAME);
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            if (character.isDPS) {
                character.addPower(skillPower);
                getBattle().AdvanceEntity(character, 100);
                lightcone.onSpecificTrigger(character, null);
                break;
            }
        }
    }

    public void useBasic() {
        this.doAttack(DamageType.BASIC, MoveType.BASIC, (e, al) -> al.hit(e, 1, TOUGHNESS_DAMAGE_SINGLE_UNIT));
    }

    public void useUltimate() {
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            AbstractPower ultPower = new TempPower();
            ultPower.setName(ULT_POWER_NAME);
            ultPower.setStat(PowerStat.ATK_PERCENT, 55);
            ultPower.setStat(PowerStat.CRIT_DAMAGE, 20 + (this.getTotalCritDamage() * 0.16f));
            ultPower.turnDuration = 2;
            character.removePower(ultPower.getName()); // remove the old power in case bronya's crit damage changed so we get new snapshot of her buff
            character.addPower(ultPower);
        }
    }

    public void onCombatStart() {
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.addPower(PermPower.create(PowerStat.DAMAGE_BONUS, 10, "Bronya Trace Damage Bonus"));
        }
        this.addPower(new BronyaBasicCritPower());
    }

    public void useTechnique() {
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.addPower(TempPower.create(PowerStat.ATK_PERCENT, 15, 2, "Bronya Technique Power"));
        }
    }

    private static class BronyaBasicCritPower extends AbstractPower {
        public BronyaBasicCritPower() {
            this.setName(this.getClass().getSimpleName());
            this.lastsForever = true;
        }

        public float setFixedCritRate(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes, float currentCrit) {
            if (damageTypes.contains(DamageType.BASIC)) {
                return 100;
            }
            return currentCrit;
        }
    }
}
