package art.ameliah.hsr.characters.yunli;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.battleLogic.log.lines.character.UseCounter;
import art.ameliah.hsr.battleLogic.log.lines.character.yunli.UseCull;
import art.ameliah.hsr.battleLogic.log.lines.character.yunli.UseSlash;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysSkillGoal;
import art.ameliah.hsr.characters.goal.shared.turn.SkillFirstTurnGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.destruction.DanceAtSunset;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TauntPower;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Yunli extends AbstractCharacter<Yunli> implements SkillFirstTurnGoal.FirstTurnTracked {

    public static final String NAME = "Yunli";
    final AbstractPower cullPower = new CullCritDamageBuff();
    final AbstractPower techniqueDamageBonus = PermPower.create(PowerStat.DAMAGE_BONUS, 80, "Technique Damage Bonus");
    final AbstractPower tauntPower = new TauntPower(this);
    private final String numNormalCountersMetricName = "Normal Counters";
    private final String num1StackCullsMetricName = "Number of Culls (1 S1 stack)";
    private final String num2StackCullsMetricName = "Number of Culls (2 S1 stacks)";
    private final String numSlashesMetricName = "Number of Slashes";
    public boolean isParrying;
    public int numSlashes = 0;
    private int numNormalCounters = 0;
    private int num1StackCulls = 0;
    private int num2StackCulls = 0;

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
    }

    public void useSkill() {
        Attack attack = this.startAttack();
        int idx = getBattle().getRandomEnemyIdx();
        getBattle().enemyCallback(idx, target -> {
            attack.hitEnemy(target, 1.2f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_TWO_UNITS, DamageType.SKILL);
        });
        getBattle().enemyCallback(idx - 1, target -> {
            attack.hitEnemy(target, 0.6f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.SKILL);
        });
        getBattle().enemyCallback(idx + 1, target -> {
            attack.hitEnemy(target, 0.6f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.SKILL);
        });
        attack.execute();
    }

    public void useBasic() {
        this.startAttack()
                .hitEnemy(getBattle().getEnemyWithHighestHP(), 1.0f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.BASIC)
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

    public void afterAttacked(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> t, int energyFromAttacked, float totalDmg) {
        addPower(getTrueSunderPower());
        if (isParrying) {
            useCull(enemy);
            removePower(cullPower);
            isParrying = false;
            for (AbstractEnemy e : getBattle().getEnemies()) {
                e.removePower(tauntPower);
            }
        } else {
            numNormalCounters++;
            getBattle().addToLog(new UseCounter(this));
            increaseEnergy(5, "from Normal Counter");

            Attack attack = this.startAttack();
            int idx = getBattle().getEnemies().indexOf(enemy);
            attack.hitEnemy(enemy, 1.2f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.FOLLOW_UP);
            getBattle().enemyCallback(idx - 1, target -> {
                attack.hitEnemy(target, 0.6f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.FOLLOW_UP);
            });
            getBattle().enemyCallback(idx + 1, target -> {
                attack.hitEnemy(target, 0.6f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.FOLLOW_UP);
            });
            attack.execute();
        }
        increaseEnergy(15, TALENT_ENERGY_GAIN);
        super.afterAttacked(character, enemy, t, energyFromAttacked, totalDmg);
    }

    public void useCull(AbstractEnemy enemy) {
        increaseEnergy(10, "from using Cull");
        AbstractPower power = new DanceAtSunset.DanceAtSunsetDamagePower();
        AbstractPower sunsetPower = getPower(power.getName());
        if (sunsetPower != null && sunsetPower.stacks == 2) {
            num2StackCulls++;
        } else {
            num1StackCulls++;
        }
        getBattle().addToLog(new UseCull(this));

        Attack attack = this.startAttack();
        int idx = getBattle().getEnemies().indexOf(enemy);
        attack.hitEnemy(enemy, 2.2f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.FOLLOW_UP, DamageType.ULTIMATE);
        getBattle().enemyCallback(idx - 1, target -> {
            attack.hitEnemy(target, 1.1f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.FOLLOW_UP, DamageType.ULTIMATE);
        });
        getBattle().enemyCallback(idx + 1, target -> {
            attack.hitEnemy(target, 1.1f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.FOLLOW_UP, DamageType.ULTIMATE);
        });

        for (int numBounces = 0; numBounces < 6; numBounces++) {
            attack.hitEnemy(getBattle().getRandomEnemy(), 0.72f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT / 4, DamageType.FOLLOW_UP, DamageType.ULTIMATE);
        }

        attack.execute();
    }

    public void useSlash(AbstractEnemy enemy) {
        increaseEnergy(10, "from using Slash");
        numSlashes++;
        getBattle().addToLog(new UseSlash(this));

        Attack attack = this.startAttack();
        int idx = getBattle().getEnemies().indexOf(enemy);
        attack.hitEnemy(enemy, 2.2f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.FOLLOW_UP, DamageType.ULTIMATE);
        getBattle().enemyCallback(idx - 1, target -> {
            attack.hitEnemy(target, 1.1f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.FOLLOW_UP, DamageType.ULTIMATE);
        });
        getBattle().enemyCallback(idx + 1, target -> {
            attack.hitEnemy(target, 1.1f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.FOLLOW_UP, DamageType.ULTIMATE);
        });

        removePower(cullPower);
        isParrying = false;
        for (AbstractEnemy e : getBattle().getEnemies()) {
            e.removePower(tauntPower);
        }

        attack.execute();
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

    public HashMap<String, String> getCharacterSpecificMetricMap() {
        HashMap<String, String> map = super.getCharacterSpecificMetricMap();
        map.put(numNormalCountersMetricName, String.valueOf(numNormalCounters));
        map.put(num1StackCullsMetricName, String.valueOf(num1StackCulls));
        map.put(num2StackCullsMetricName, String.valueOf(num2StackCulls));
        map.put(numSlashesMetricName, String.valueOf(numSlashes));
        return map;
    }

    public ArrayList<String> getOrderedCharacterSpecificMetricsKeys() {
        ArrayList<String> list = super.getOrderedCharacterSpecificMetricsKeys();
        list.add(numNormalCountersMetricName);
        list.add(num1StackCullsMetricName);
        list.add(num2StackCullsMetricName);
        list.add(numSlashesMetricName);
        return list;
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
