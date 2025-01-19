package art.ameliah.hsr.characters.abundance.lingsha;

import art.ameliah.hsr.battleLogic.AbstractSummon;
import art.ameliah.hsr.battleLogic.FuYuan;
import art.ameliah.hsr.battleLogic.combat.enemy.EnemyAttackLogic;
import art.ameliah.hsr.battleLogic.combat.result.HitResult;
import art.ameliah.hsr.battleLogic.log.lines.character.EmergencyHeal;
import art.ameliah.hsr.battleLogic.log.lines.character.lingsha.FuYuanGain;
import art.ameliah.hsr.battleLogic.log.lines.character.lingsha.FuYuanLose;
import art.ameliah.hsr.battleLogic.log.lines.character.lingsha.HitSinceLastHeal;
import art.ameliah.hsr.battleLogic.log.lines.character.lingsha.ResetTracker;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.AbstractSummoner;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.turn.UseExcessSkillPointsGoal;
import art.ameliah.hsr.characters.goal.shared.ult.DontUltNumby;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.metrics.CounterMetric;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TracePower;
import art.ameliah.hsr.utils.Randf;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lingsha extends AbstractSummoner<Lingsha> {

    public static final String NAME = "Lingsha";

    protected CounterMetric<Integer> fuYuanAttackCounter = metricRegistry.register(CounterMetric.newIntegerCounter("lingsha-fy-attacks", "Number of Fu Yuan Attacks"));
    protected CounterMetric<Integer> emergencyHealsCounter = metricRegistry.register(CounterMetric.newIntegerCounter("lingsha-emergencyHeals", "Number of Emergency Heals"));

    static final int fuYuanMaxHitCount = 5;
    static final int skillHitCountGain = 3;
    private static final int emergencyHealCooldown = 2;
    final FuYuan fuYuan;
    final AbstractPower damageTrackerPower;
    private final HashMap<AbstractCharacter<?>, Integer> characterTimesDamageTakenMap = new HashMap<>();
    int fuYuanCurrentHitCount = 0;
    private int currentEmergencyHealCD = 0;

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

        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));
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
            return super.getFinalAttack() + ((baseAtk + lightcone.baseAtk) * (1 + atkBonus / 100));
        }
    }

    public void useBasic() {
        this.doAttack(DamageType.BASIC,
                dh -> dh.logic(this.getTarget(MoveType.BASIC), (e, al) -> al.hit(e, 1, TOUGHNESS_DAMAGE_SINGLE_UNIT)));
    }

    public void useSkill() {
        this.startAttack()
                .handle(DamageType.SKILL,
                        dh -> dh.logic(getBattle().getEnemies(), (targets, al) -> al.hit(targets, 0.8f, TOUGHNESS_DAMAGE_SINGLE_UNIT)))
                .afterAttackHook(() -> {
                    increaseHitCount(skillHitCountGain);
                    getBattle().AdvanceEntity(fuYuan, 20);
                    fuYuan.speedPriority = 1;
                    resetDamageTracker();
                }).execute();
    }

    public void useUltimate() {
        this.startAttack()
                .handle(DamageType.ULTIMATE, dh -> dh.logic(getBattle().getEnemies(), (targets, al) -> {
                    for (AbstractEnemy target : targets) {
                        target.addPower(new Befog());
                        al.hit(target, 1.5f, TOUGHNESS_DAMAGE_TWO_UNITS);
                    }
                })).afterAttackHook(() -> getBattle().AdvanceEntity(fuYuan, 100)).execute();
    }

    public void FuYuanAttack(boolean useHitCount) {
        this.startAttack().handle(DamageType.FOLLOW_UP, dh -> dh.logic(getBattle().getEnemies(), (targets, al) -> {
            Collection<AbstractEnemy> nonBroken = al.hit(targets, 0.75f, TOUGHNESS_DAMAGE_SINGLE_UNIT)
                    .stream()
                    .map(HitResult::getEnemy)
                    .filter(enemy -> !enemy.isWeaknessBroken())
                    .toList();

            if (nonBroken.isEmpty()) {
                al.hit(getBattle().getRandomEnemy(), 0.75f, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            } else {
                var tryFireEnemy = nonBroken.stream().filter(e -> e.hasWeakness(ElementType.FIRE)).findAny();
                tryFireEnemy.ifPresentOrElse(
                        e -> al.hit(e, 0.75f, TOUGHNESS_DAMAGE_SINGLE_UNIT),
                        () -> al.hit(Randf.rand(nonBroken, getBattle().getEnemyTargetRng()), 0.75f, TOUGHNESS_DAMAGE_SINGLE_UNIT)
                );
            }
        })).afterAttackHook(() -> {
            this.fuYuanAttackCounter.increment();
            if (useHitCount) {
                decreaseHitCount(1);
            }
            resetDamageTracker();
        }).execute();
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
        getBattle().registerForPlayers(p -> {
            characterTimesDamageTakenMap.put(p, 0);
            p.addPower(damageTrackerPower);
        });
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

    @Override
    public String leftOverAV() {
        return super.leftOverAV()
                + String.format("\nLeft over AV: %.2f (FuYuan)", getBattle().getActionValueMap().get(this.fuYuan));
    }

    @Override
    public List<AbstractSummon<Lingsha>> getSummons() {
        return Collections.singletonList(fuYuan);
    }

    private static class Befog extends AbstractPower {
        public Befog() {
            this.setName(this.getClass().getSimpleName());
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
            this.setName(this.getClass().getSimpleName());
        }

        @Override
        public void afterAttacked(EnemyAttackLogic attack) {
            attack.getTargets().forEach(t -> {
                int timesHit = characterTimesDamageTakenMap.merge(t, 1, Integer::sum);
                getBattle().addToLog(new HitSinceLastHeal(t, timesHit));
            });

            boolean trigger = characterTimesDamageTakenMap.values().stream().anyMatch(v -> v >= 2);
            if (!trigger || currentEmergencyHealCD > 0) {
                return;
            }

            getBattle().addToLog(new EmergencyHeal(Lingsha.this));
            Lingsha.this.emergencyHealsCounter.increment();
            currentEmergencyHealCD = emergencyHealCooldown;
            Lingsha.this.FuYuanAttack(false);
        }
    }
}
