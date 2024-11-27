package art.ameliah.hsr.characters.yunli;

import art.ameliah.hsr.battleLogic.combat.ally.DelayAttack;
import art.ameliah.hsr.battleLogic.combat.enemy.EnemyAttackLogic;
import art.ameliah.hsr.battleLogic.log.lines.character.UseCounter;
import art.ameliah.hsr.battleLogic.log.lines.character.yunli.UseCull;
import art.ameliah.hsr.battleLogic.log.lines.character.yunli.UseSlash;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysSkillGoal;
import art.ameliah.hsr.characters.goal.shared.turn.SkillFirstTurnGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.destruction.DanceAtSunset;
import art.ameliah.hsr.metrics.CounterMetric;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TauntPower;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;
import art.ameliah.hsr.utils.Randf;

import java.util.Collection;
import java.util.List;

import static art.ameliah.hsr.characters.DamageType.*;

public class Yunli extends AbstractCharacter<Yunli> implements SkillFirstTurnGoal.FirstTurnTracked {

    public static final String NAME = "Yunli";

    private final AbstractPower cullPower = new CullCritDamageBuff();
    private final AbstractPower techniqueDamageBonus = PermPower.create(PowerStat.DAMAGE_BONUS, 80, "Technique Damage Bonus");
    private final AbstractPower tauntPower = new TauntPower(this);

    protected CounterMetric<Integer> normalCount = metricRegistry.register(CounterMetric.newIntegerCounter("yunli-normal-counters", "Normal counter"));

    protected CounterMetric<Integer> num1StackCulls = metricRegistry.register(CounterMetric.newIntegerCounter("yunli-num-1-stack-culls", "Number of Culls (1 S1 stack)"));

    protected CounterMetric<Integer> num2StackCulls = metricRegistry.register(CounterMetric.newIntegerCounter("yunli-num-2-stack-culls", "Number of Culls (2 S1 stacks)"));

    protected CounterMetric<Integer> numSlashesMetric = metricRegistry.register(CounterMetric.newIntegerCounter("yunli-num-slashes", "Number of Slashes"));

    public boolean isParrying;


    public Yunli() {
        super(NAME, 1358, 679, 461, 94, 80, ElementType.PHYSICAL, 240, 125, Path.DESTRUCTION);

        this.ultCost = 120;
        this.isDPS = true;

        this.addPower(new TracePower()
                .setStat(PowerStat.ATK_PERCENT, 28)
                .setStat(PowerStat.CRIT_CHANCE, 6.7f)
                .setStat(PowerStat.HP_PERCENT, 18));

        this.registerGoal(0, new YunliEmergancyUlt(this));
        this.registerGoal(10, new UltIfNextIsEnemy(this));
        this.registerGoal(0, new SkillFirstTurnGoal<>(this));
        this.registerGoal(10, new AlwaysSkillGoal<>(this, 1));
        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));
    }

    public void useSkill() {
        this.startAttack()
                .handle(dh -> {
                    AbstractEnemy target = this.getTarget(MoveType.SKILL);
                    int idx = getBattle().getEnemies().indexOf(target);

                    dh.addTypes(SKILL);
                    dh.logic(target, al -> al.hit(target, 1.2f, TOUGHNESS_DAMAGE_TWO_UNITS));
                    dh.logic(idx - 1, (e, al) -> al.hit(e, 0.6f, TOUGHNESS_DAMAGE_SINGLE_UNIT));
                    dh.logic(idx + 1, (e, al) -> al.hit(e, 0.6f, TOUGHNESS_DAMAGE_SINGLE_UNIT));
                }).execute();
    }

    public void useBasic() {
        this.startAttack()
                .handle(dh -> {
                    AbstractEnemy target = this.getTarget(MoveType.BASIC);

                    dh.addTypes(BASIC);
                    dh.logic(target, al -> al.hit(this.getTarget(MoveType.BASIC), 1, TOUGHNESS_DAMAGE_SINGLE_UNIT));
                })
                .execute();
    }

    public void useUltimate() {
        isParrying = true;
        addPower(cullPower);

        tauntPower.lastsForever = true; // used so I can manually remove later
        for (AbstractEnemy enemy : getBattle().getEnemies()) {
            enemy.addPower(tauntPower);
        }
    }

    @Override
    public void afterAttacked(EnemyAttackLogic enemyAttack) {
        addPower(getTrueSunderPower());
        if (isParrying) {
            useCull(enemyAttack.getSource());
        } else {
            this.normalCount.increment();
            getBattle().addToLog(new UseCounter(this));
            increaseEnergy(5, "from Normal Counter");

            this.startAttack()
                    .handle(dh -> {
                        int idx = getBattle().getEnemies().indexOf(enemyAttack.getSource());
                        if (idx == -1) {
                            idx = getBattle().getRandomEnemyIdx();
                        }

                        dh.addTypes(FOLLOW_UP);
                        dh.logic(idx - 1, (e, al) -> al.hit(e, 0.6f, TOUGHNESS_DAMAGE_SINGLE_UNIT));
                        dh.logic(idx, (e, al) -> al.hit(e, 1.2f, TOUGHNESS_DAMAGE_SINGLE_UNIT));
                        dh.logic(idx + 1, (e, al) -> al.hit(e, 0.6f, TOUGHNESS_DAMAGE_SINGLE_UNIT));
                    }).execute();
        }
        increaseEnergy(10, TALENT_ENERGY_GAIN);
    }

    public void useCull(AbstractEnemy enemy) {
        increaseEnergy(10, "from using Cull");
        AbstractPower power = new DanceAtSunset.DanceAtSunsetDamagePower();
        AbstractPower sunsetPower = getPower(power.getName());
        if (sunsetPower != null && sunsetPower.stacks == 2) {
            this.num2StackCulls.increment();
        } else {
            this.num1StackCulls.increment();
        }
        getBattle().addToLog(new UseCull(this));

        this.startAttack()
                .handle(dh -> {
                    this.baseIntuit(enemy, dh);

                    Collection<AbstractEnemy> targets = Randf.rand(getBattle().getEnemies(), 6, getBattle().getEnemyTargetRng());
                    dh.addEnemies(targets);
                    dh.logic(al -> {
                        for (var target : targets) {
                            al.hit(target, 0.72f, TOUGHNESS_DAMAGE_SINGLE_UNIT / 4);
                        }
                    });
                }).afterAttackHook(this::cleanUpParry)
                .execute();
    }

    public void useSlash(AbstractEnemy enemy) {
        increaseEnergy(10, "from using Slash");
        this.numSlashesMetric.increment();
        getBattle().addToLog(new UseSlash(this));

        this.startAttack()
                .handle(dh -> baseIntuit(enemy, dh))
                .afterAttackHook(this::cleanUpParry)
                .execute();
    }

    private void baseIntuit(AbstractEnemy enemy, DelayAttack dh) {
        dh.addTypes(FOLLOW_UP, ULTIMATE);
        int idx = getBattle().getEnemies().indexOf(enemy);

        dh.logic(idx - 1, (e, al) -> al.hit(e, 1.1f, TOUGHNESS_DAMAGE_SINGLE_UNIT));
        dh.logic(idx, (e, al) -> al.hit(e, 2.2f, TOUGHNESS_DAMAGE_TWO_UNITS));
        dh.logic(idx + 1, (e, al) -> al.hit(e, 1.1f, TOUGHNESS_DAMAGE_SINGLE_UNIT));
    }

    private void cleanUpParry() {
        this.removePower(cullPower);
        this.isParrying = false;
        getBattle().getEnemies().forEach(e -> e.removePower(this.tauntPower));
    }

    public void useTechnique() {
        addPower(getTrueSunderPower());
        addPower(techniqueDamageBonus);
        useCull(getBattle().getRandomEnemy());
        removePower(techniqueDamageBonus);
    }

    public AbstractPower getTrueSunderPower() {
        return TempPower.create(PowerStat.ATK_PERCENT, 30, 1, "True Sunder Atk Bonus");
    }

    @Override
    public boolean isFirstTurn() {
        return firstMove;
    }

    @Override
    public void setFirstTurn(boolean firstTurn) {
        firstMove = firstTurn;
    }

    private static class CullCritDamageBuff extends AbstractPower {
        public CullCritDamageBuff() {
            this.setName(this.getClass().getSimpleName());
            this.lastsForever = true;
        }

        @Override
        public float getConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.FOLLOW_UP) {
                    return 100;
                }
            }
            return 0;
        }
    }
}
