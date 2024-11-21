package art.ameliah.hsr.characters.lingsha;

import art.ameliah.hsr.battleLogic.AbstractSummon;
import art.ameliah.hsr.battleLogic.BattleHelpers;
import art.ameliah.hsr.battleLogic.FuYuan;
import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.battleLogic.log.lines.character.EmergencyHeal;
import art.ameliah.hsr.battleLogic.log.lines.character.lingsha.FuYuanGain;
import art.ameliah.hsr.battleLogic.log.lines.character.lingsha.FuYuanLose;
import art.ameliah.hsr.battleLogic.log.lines.character.lingsha.HitSinceLastHeal;
import art.ameliah.hsr.battleLogic.log.lines.character.lingsha.ResetTracker;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.AbstractSummoner;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.DontUltNumby;
import art.ameliah.hsr.characters.goal.shared.UseExcessSkillPointsGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TracePower;
import art.ameliah.hsr.utils.Randf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lingsha extends AbstractSummoner<Lingsha> {
    public static final String NAME = "Lingsha";

    final FuYuan fuYuan;
    final AbstractPower damageTrackerPower;
    static final int fuYuanMaxHitCount = 5;
    static final int skillHitCountGain = 3;
    int fuYuanCurrentHitCount = 0;
    private static final int emergencyHealCooldown = 2;
    private int currentEmergencyHealCD = 0;
    private final HashMap<AbstractCharacter<?>, Integer> characterTimesDamageTakenMap = new HashMap<>();
    private int fuYuanAttacksMetric = 0;
    private final String fuYuanAttacksMetricName = "Number of Fu Yuan Attacks";
    private int numEmergencyHeals = 0;
    private final String numEmergencyHealsMetricName = "Number of Emergency Heal Triggers";
    private final String leftoverAVFuYuanMetricName = "Leftover AV (Fu Yuan)";

    public Lingsha() {
        super(NAME, 1358, 679, 437, 98, 80, ElementType.FIRE, 110, 100, Path.ABUNDANCE);

        this.addPower(new TracePower()
                .setStat(PowerStat.HP_PERCENT, 18)
                .setStat(PowerStat.BREAK_EFFECT, 37.3f)
                .setStat(PowerStat.ATK_PERCENT, 10));

        this.hasAttackingUltimate = true;
        this.basicEnergyGain = 30;

        damageTrackerPower = new LingshaEmergencyHealTracker();
        fuYuan = new FuYuan(this);

        this.registerGoal(0, new DontUltNumby<>(this));
        this.registerGoal(10, new LingshaUltGoal(this));

        this.registerGoal(0, new UseExcessSkillPointsGoal<>(this));
        this.registerGoal(10, new LingshaTurnGoal(this));
    }

    @Override
    public float getFinalAttack() {
        if (!getBattle().inCombat()) {
            return super.getFinalAttack();
        } else {
            float atkBonus = 0.25f * getTotalBreakEffect();
            if (atkBonus > 50) {
                atkBonus = 50;
            }
            return super.getFinalAttack() + ((baseAtk + lightcone.baseAtk) *  (1 + atkBonus / 100));
        }
    }

    public void useBasic() {
        this.startAttack()
                .hitEnemy(getBattle().getRandomEnemy(), 1.0f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.BASIC)
                .execute();
    }

    public void useSkill() {
        Attack attack = this.startAttack();
        getBattle().getEnemies().forEach(target -> {
            attack.hitEnemy(target, 0.8f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.SKILL);
        });

        increaseHitCount(skillHitCountGain);
        getBattle().AdvanceEntity(fuYuan, 20);
        fuYuan.speedPriority = 1;
        resetDamageTracker();

        attack.execute();
    }

    public void useUltimate() {
        Attack attack = this.startAttack();
        getBattle().getEnemies().forEach(target -> {
            target.addPower(new Befog());
            attack.hitEnemy(target, 1.5f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_TWO_UNITS, DamageType.ULTIMATE);
        });
        getBattle().AdvanceEntity(fuYuan, 100);

        attack.execute();
    }

    public void FuYuanAttack(boolean useHitCount) {
        fuYuanAttacksMetric++;

        List<AbstractEnemy> nonBrokenEnemies = new ArrayList<>();

        Attack attack = this.startAttack();
        getBattle().getEnemies().forEach(target -> {
            attack.hitEnemy(target, 0.75f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.FOLLOW_UP);
            if (!target.isWeaknessBroken()) {
                nonBrokenEnemies.add(target);
            }
        });

        if (nonBrokenEnemies.isEmpty()) {
            attack.hitEnemy(getBattle().getRandomEnemy(), 0.75f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.FOLLOW_UP);
        } else {
            AbstractEnemy target = Randf.rand(nonBrokenEnemies, getBattle().getGetRandomEnemyRng());
            attack.hitEnemy(target, 0.75f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.FOLLOW_UP);
        }

        if (useHitCount) {
            decreaseHitCount(1);
        }
        resetDamageTracker();

        attack.execute();
    }

    private void increaseHitCount(int amount) {
        if (fuYuanMaxHitCount <= 0) {
            getBattle().getActionValueMap().put(fuYuan, fuYuan.getBaseAV());
        }
        int initalStack = fuYuanCurrentHitCount;
        fuYuanCurrentHitCount += amount;
        if (fuYuanCurrentHitCount > fuYuanMaxHitCount) {
            fuYuanCurrentHitCount = fuYuanMaxHitCount;
        }
        getBattle().addToLog(new FuYuanGain(amount, initalStack, fuYuanCurrentHitCount));
    }

    private void decreaseHitCount(int amount) {
        int initalStack = fuYuanCurrentHitCount;
        fuYuanCurrentHitCount -= amount;
        if (fuYuanCurrentHitCount <= 0) {
            fuYuanCurrentHitCount = 0;
            getBattle().getActionValueMap().remove(fuYuan);
        }
        getBattle().addToLog(new FuYuanLose(amount, initalStack, fuYuanCurrentHitCount));
    }

    public void onTurnStart() {
        if (currentEmergencyHealCD > 0) {
            currentEmergencyHealCD--;
        }
    }

    public void onCombatStart() {
        getBattle().getActionValueMap().put(fuYuan, fuYuan.getBaseAV());
        increaseHitCount(skillHitCountGain);
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            characterTimesDamageTakenMap.put(character, 0);
            character.addPower(damageTrackerPower);
        }
    }

    public void useTechnique() {
        for (AbstractEnemy enemy : getBattle().getEnemies()) {
            AbstractPower befog = new Befog();
            enemy.addPower(befog);
        }
    }

    public void resetDamageTracker() {
        getBattle().addToLog(new ResetTracker());
        for (Map.Entry<AbstractCharacter<?>, Integer> entry : characterTimesDamageTakenMap.entrySet()) {
            entry.setValue(0);
        }
    }

    public HashMap<String, String> getCharacterSpecificMetricMap() {
        HashMap<String, String> map = super.getCharacterSpecificMetricMap();
        map.put(leftoverAVFuYuanMetricName, String.valueOf(getBattle().getActionValueMap().get(fuYuan)));
        map.put(fuYuanAttacksMetricName, String.valueOf(fuYuanAttacksMetric));
        map.put(numEmergencyHealsMetricName, String.valueOf(numEmergencyHeals));
        return map;
    }

    public ArrayList<String> getOrderedCharacterSpecificMetricsKeys() {
        ArrayList<String> list = super.getOrderedCharacterSpecificMetricsKeys();
        list.add(leftoverAVFuYuanMetricName);
        list.add(fuYuanAttacksMetricName);
        list.add(numEmergencyHealsMetricName);
        return list;
    }

    @Override
    public List<AbstractSummon<Lingsha>> getSummons() {
        return Collections.singletonList(fuYuan);
    }

    private static class Befog extends AbstractPower {
        public Befog() {
            this.name = this.getClass().getSimpleName();
            this.turnDuration = 2;
            this.type = PowerType.DEBUFF;
        }

        @Override
        public float getConditionalDamageTaken(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (damageTypes.contains(DamageType.BREAK)) {
                return 25f;
            }
            return 0;
        }
    }

    private class LingshaEmergencyHealTracker extends PermPower {
        public LingshaEmergencyHealTracker() {
            this.name = this.getClass().getSimpleName();
        }

        @Override
        public void onAttacked(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> types, int energyToGain, float totalDmg) {
            int timesHit = characterTimesDamageTakenMap.get(character);
            timesHit++;
            getBattle().addToLog(new HitSinceLastHeal(character, timesHit));
            if (timesHit >= 2 && currentEmergencyHealCD <= 0) {
                getBattle().addToLog(new EmergencyHeal(Lingsha.this));
                numEmergencyHeals++;
                currentEmergencyHealCD = emergencyHealCooldown;
                Lingsha.this.FuYuanAttack(false);
            } else {
                characterTimesDamageTakenMap.put(character, timesHit);
            }
        }
    }
}
