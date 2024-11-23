package art.ameliah.hsr.characters.tingyun;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.combat.AllyHit;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.AlwaysUltGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;
import art.ameliah.hsr.utils.Randf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tingyun extends AbstractCharacter<Tingyun> {
    public static final String NAME = "Tingyun";

    AbstractCharacter<?> benefactor;
    public int skillProcs = 0;
    public int talentProcs = 0;
    private final String skillProcsMetricName = "Skill Extra Damage Procs";
    private final String talentProcsMetricName = "Talent Extra Damage Procs";

    public Tingyun() {
        super(NAME, 847, 529, 397, 112, 80, ElementType.LIGHTNING, 130, 100, Path.HARMONY);

        this.addPower(new TracePower()
                .setStat(PowerStat.ATK_PERCENT, 28)
                .setStat(PowerStat.DEF_PERCENT, 22.5f)
                .setStat(PowerStat.LIGHTNING_DMG_BOOST, 8));

        this.registerGoal(0, new AlwaysUltGoal<>(this));
        this.registerGoal(0, new TingyunTurnGoal(this));
    }

    public void useSkill() {
        TingyunSkillPower skillPower = new TingyunSkillPower();
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            if (character.isDPS) {
                benefactor = character;
                character.addPower(skillPower);
                break;
            }
        }

        TempPower speedPower = TempPower.create(PowerStat.SPEED_PERCENT, 20, 1, "Tingyun Skill Speed Power");
        speedPower.justApplied = true;
        getBattle().IncreaseSpeed(this, speedPower);
    }
    public void useBasic() {
        Attack attack = this.startAttack();
        AbstractEnemy target = getBattle().getEnemyWithHighestHP();

        attack.hitEnemy(target, 1.1f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.BASIC);
        if (benefactor != null) {
            talentProcs++;
            attack.hitEnemy(benefactor, target, 0.66F, MultiplierStat.ATK);
        }

        attack.execute();
    }

    public void useUltimate() {
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            if (character.isDPS && character.currentEnergy < character.maxEnergy) {
                character.increaseEnergy(60, false, "from Tingyun Ult");
                character.addPower(TempPower.create(PowerStat.DAMAGE_BONUS, 56, 2, "Tingyun Ult Damage Bonus"));
                break;
            }
        }
    }

    public void onTurnStart() {
        increaseEnergy(5, TRACE_ENERGY_GAIN);
        tryUltimate();
    }

    public void useTechnique() {
        increaseEnergy(maxEnergy, false, TECHNIQUE_ENERGY_GAIN);
    }

    public void onCombatStart() {
        addPower(new TingyunBonusBasicDamagePower());
    }

    public HashMap<String, String> getCharacterSpecificMetricMap() {
        HashMap<String, String> map = super.getCharacterSpecificMetricMap();
        map.put(skillProcsMetricName, String.valueOf(skillProcs));
        map.put(talentProcsMetricName, String.valueOf(talentProcs));
        return map;
    }

    public ArrayList<String> getOrderedCharacterSpecificMetricsKeys() {
        ArrayList<String> list = super.getOrderedCharacterSpecificMetricsKeys();
        list.add(skillProcsMetricName);
        list.add(talentProcsMetricName);
        return list;
    }

    private class TingyunSkillPower extends TempPower {
        public TingyunSkillPower() {
            super(3);

            this.name = this.getClass().getSimpleName();
            this.setStat(PowerStat.ATK_PERCENT, 55);
        }

        public void onAttack(Attack attack) {
            skillProcs++;
            AbstractEnemy target = Randf.rand(attack.getTargets(), getBattle().getGetRandomEnemyRng());
            attack.hitEnemy(new AllyHit(attack.getSource(), target, 0.64F, MultiplierStat.ATK, List.of(), 0, ElementType.LIGHTNING, false));
        }

        public void onUseUltimate() {
            if (benefactor != null) {
                getBattle().IncreaseSpeed(benefactor, TempPower.create(PowerStat.SPEED_PERCENT, 20, 1, "Tingyun E1 Speed Power"));
            }
        }

        public void onRemove() {
            benefactor = null;
        }
    }

    private static class TingyunBonusBasicDamagePower extends AbstractPower {
        public TingyunBonusBasicDamagePower() {
            this.name = this.getClass().getSimpleName();
            this.lastsForever = true;
        }
        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.BASIC) {
                    return 40;
                }
            }
            return 0;
        }
    }
}
