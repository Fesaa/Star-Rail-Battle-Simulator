package art.ameliah.hsr.characters.robin;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.Concerto;
import art.ameliah.hsr.battleLogic.combat.AttackLogic;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.turn.SkillCounterTurnGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.characters.goal.shared.ult.DontUltNumby;
import art.ameliah.hsr.characters.goal.shared.ult.UltAtEndOfBattle;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TracePower;
import art.ameliah.hsr.utils.Randf;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Robin extends AbstractCharacter<Robin> implements SkillCounterTurnGoal.SkillCounterCharacter {

    public static final String NAME = "Robin";
    public static final String ULT_POWER_NAME = "RobinUltPower";
    final PermPower skillPower = PermPower.create(PowerStat.DAMAGE_BONUS, 50, "Robin Skill Power");
    final RobinUltPower ultPower = new RobinUltPower();
    final RobinFixedCritPower fixedCritPower = new RobinFixedCritPower();
    final Concerto concerto = new Concerto(this);
    private final String allyAttacksMetricName = "Number of Ally Attacks";
    private final String concertoProcsMetricName = "Number of Concerto Hits";
    private int skillCounter = 0;
    private int allyAttacksMetric = 0;
    private int concertoProcs = 0;

    public Robin() {
        super(NAME, 1281, 640, 485, 102, 80, ElementType.PHYSICAL, 160, 100, Path.HARMONY);

        this.skillEnergyGain = 35;

        this.addPower(new TracePower()
                .setStat(PowerStat.ATK_PERCENT, 28)
                .setStat(PowerStat.HP_PERCENT, 18)
                .setStat(PowerStat.FLAT_SPEED, 5));

        this.registerGoal(0, new SkillCounterTurnGoal<>(this));

        this.registerGoal(0, new UltAtEndOfBattle<>(this));
        this.registerGoal(10, new Robin0AVUltGoal(this));
        this.registerGoal(20, new DontUltNumby<>(this));
        this.registerGoal(30, new RobinBroynaFeixiaoUltGoal(this));
        this.registerGoal(40, new RobinDPSUltGoal(this));
        this.registerGoal(100, new AlwaysUltGoal<>(this));

        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));
    }

    public void useSkill() {
        skillCounter = 3;
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.addPower(skillPower);
        }
    }

    public void useBasic() {
        this.startAttack()
                .handle(dh -> {
                    AbstractEnemy target = this.getTarget(MoveType.BASIC);
                    dh.addTypes(DamageType.BASIC);
                    dh.logic(target, al -> al.hit(this.getTarget(MoveType.BASIC), 1, TOUGHNESS_DAMAGE_SINGLE_UNIT));
                }).execute();
    }

    public void useUltimate() {
        AbstractEntity slowestAlly = null;
        float slowestAV = -1;
        AbstractEntity fastestAlly = null;
        float fastestAV = -1;
        AbstractEntity middleAlly = null;
        for (Map.Entry<AbstractEntity, Float> entry : getBattle().getActionValueMap().entrySet()) {
            if (entry.getKey() instanceof AbstractCharacter && !(entry.getKey() instanceof Robin)) {
                if (slowestAlly == null) {
                    slowestAlly = entry.getKey();
                    fastestAlly = entry.getKey();
                    middleAlly = entry.getKey();
                    slowestAV = entry.getValue();
                    fastestAV = entry.getValue();
                } else {
                    if (entry.getValue() < fastestAV) {
                        fastestAlly = entry.getKey();
                        fastestAV = entry.getValue();
                    } else if (entry.getValue() > slowestAV) {
                        slowestAlly = entry.getKey();
                        slowestAV = entry.getValue();
                    }
                }
            }
        }
        for (Map.Entry<AbstractEntity, Float> entry : getBattle().getActionValueMap().entrySet()) {
            if (entry.getKey() instanceof AbstractCharacter && !(entry.getKey() instanceof Robin)) {
                if (entry.getKey() != fastestAlly && entry.getKey() != slowestAlly) {
                    middleAlly = entry.getKey();
                }
            }
        }

        // preserves the order in which allies go next based on their original AVs
        // most recently advanced ally will go first
        getBattle().AdvanceEntity(slowestAlly, 100);
        getBattle().AdvanceEntity(middleAlly, 100);
        getBattle().AdvanceEntity(fastestAlly, 100);

        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.addPower(ultPower);
        }
        this.addPower(fixedCritPower);
        getBattle().getActionValueMap().remove(this);
        getBattle().getActionValueMap().put(concerto, concerto.getBaseAV());
    }

    public void onCombatStart() {
        getBattle().AdvanceEntity(this, 25);
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.addPower(new RobinTalentPower());
        }
    }

    public void onTurnStart() {
        if (skillCounter > 0) {
            skillCounter--;
            if (skillCounter <= 0) {
                for (AbstractCharacter<?> character : getBattle().getPlayers()) {
                    character.removePower(skillPower);
                }
            }
        }
    }

    public void useTechnique() {
        increaseEnergy(5, TECHNIQUE_ENERGY_GAIN);
    }

    public void addPower(@NotNull AbstractPower power) {
        super.addPower(power);
        ultPower.updateAtkBuff();
    }

    public void onConcertoEnd() {
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.removePower(ultPower);
        }
        this.removePower(fixedCritPower);
    }

    public HashMap<String, String> getCharacterSpecificMetricMap() {
        HashMap<String, String> map = super.getCharacterSpecificMetricMap();
        map.put(allyAttacksMetricName, String.valueOf(allyAttacksMetric));
        map.put(concertoProcsMetricName, String.valueOf(concertoProcs));
        return map;
    }

    public ArrayList<String> getOrderedCharacterSpecificMetricsKeys() {
        ArrayList<String> list = super.getOrderedCharacterSpecificMetricsKeys();
        list.add(allyAttacksMetricName);
        list.add(concertoProcsMetricName);
        return list;
    }

    public HashMap<String, String> addLeftoverCharacterAVMetric(HashMap<String, String> metricMap) {
        Float leftoverAV = getBattle().getActionValueMap().get(this);
        if (leftoverAV == null) {
            leftoverAV = getBattle().getActionValueMap().get(concerto);
            metricMap.put(leftoverAVMetricName, String.format("%.2f (Concerto)", leftoverAV));
        } else {
            return super.addLeftoverCharacterAVMetric(metricMap);
        }

        return metricMap;
    }

    @Override
    public int getSkillCounter() {
        return skillCounter;
    }

    private static class RobinFixedCritPower extends AbstractPower {
        public RobinFixedCritPower() {
            this.setName(this.getClass().getSimpleName());
            lastsForever = true;
        }

        @Override
        public float setFixedCritRate(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes, float currentCrit) {
            return 100;
        }

        @Override
        public float setFixedCritDmg(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes, float currentCritDmg) {
            return 150;
        }
    }

    private class RobinTalentPower extends PermPower {
        public RobinTalentPower() {
            this.setName(this.getClass().getSimpleName());
            this.setStat(PowerStat.CRIT_DAMAGE, 20);
        }

        @Override
        public void beforeAttack(AttackLogic attack) {
            Robin.this.increaseEnergy(2, TALENT_ENERGY_GAIN);
            Robin.this.allyAttacksMetric++;
        }
    }

    private class RobinUltPower extends AbstractPower {
        public RobinUltPower() {
            this.setName(ULT_POWER_NAME);
            lastsForever = true;
        }

        public void updateAtkBuff() {
            float atk = getFinalAttackWithoutConcerto();
            this.setStat(PowerStat.FLAT_ATK, (int) (0.228 * atk) + 200);
        }

        private float getFinalAttackWithoutConcerto() {
            return Robin.this.getFinalAttack() - this.getStat(PowerStat.FLAT_ATK);
        }

        @Override
        public void afterAttack(AttackLogic attack) {
            AbstractEnemy target = Randf.rand(attack.getTargets(), getBattle().getGetRandomEnemyRng());
            if (target == null) {
                return;
            }

            attack.hit(Robin.this, target, 1.2f, MultiplierStat.ATK, 0, ElementType.PHYSICAL, false, List.of());
            concertoProcs++;
        }

        @Override
        public float getConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (damageTypes.contains(DamageType.FOLLOW_UP)) {
                return 25;
            }
            return 0;
        }
    }
}
