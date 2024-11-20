package art.ameliah.hsr.characters.yunli;

import art.ameliah.hsr.battleLogic.BattleHelpers;
import art.ameliah.hsr.battleLogic.log.lines.character.UseCounter;
import art.ameliah.hsr.battleLogic.log.lines.character.yunli.UseCull;
import art.ameliah.hsr.battleLogic.log.lines.character.yunli.UseSlash;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.AlwaysSkillGoal;
import art.ameliah.hsr.characters.goal.shared.SkillFirstTurnGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import java.util.ArrayList;
import java.util.HashMap;
import art.ameliah.hsr.lightcones.destruction.DanceAtSunset;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TauntPower;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;

public class Yunli extends AbstractCharacter<Yunli> implements SkillFirstTurnGoal.FirstTurnTracked {
    
    public static String NAME = "Yunli";

    public boolean isParrying;
    AbstractPower cullPower = new CullCritDamageBuff();
    AbstractPower techniqueDamageBonus = PermPower.create(PowerStat.DAMAGE_BONUS, 80, "Technique Damage Bonus");
    AbstractPower tauntPower = new TauntPower(this);

    private int numNormalCounters = 0;
    private int num1StackCulls = 0;
    private int num2StackCulls = 0;
    public int numSlashes = 0;
    private String numNormalCountersMetricName = "Normal Counters";
    private String num1StackCullsMetricName = "Number of Culls (1 S1 stack)";
    private String num2StackCullsMetricName = "Number of Culls (2 S1 stacks)";
    private String numSlashesMetricName = "Number of Slashes";

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
        ArrayList<DamageType> types = new ArrayList<>();
        types.add(DamageType.SKILL);
        getBattle().getHelper().PreAttackLogic(this, types);

        if (getBattle().getEnemies().size() >= 3) {
            int middleIndex = getBattle().getEnemies().size() / 2;
            getBattle().getHelper().hitEnemy(this, getBattle().getEnemies().get(middleIndex), 1.2f, BattleHelpers.MultiplierStat.ATK, types, TOUGHNESS_DAMAGE_TWO_UNITS);
            getBattle().getHelper().hitEnemy(this, getBattle().getEnemies().get(middleIndex + 1), 0.6f, BattleHelpers.MultiplierStat.ATK,types, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            getBattle().getHelper().hitEnemy(this, getBattle().getEnemies().get(middleIndex - 1), 0.6f, BattleHelpers.MultiplierStat.ATK, types, TOUGHNESS_DAMAGE_SINGLE_UNIT);
        } else {
            AbstractEnemy enemy = getBattle().getEnemies().get(0);
            getBattle().getHelper().hitEnemy(this, enemy, 1.2f, BattleHelpers.MultiplierStat.ATK, types, TOUGHNESS_DAMAGE_TWO_UNITS);
            if (getBattle().getEnemies().size() == 2) {
                getBattle().getHelper().hitEnemy(this, getBattle().getEnemies().get(1), 0.6f, BattleHelpers.MultiplierStat.ATK, types, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            }
        }
        getBattle().getHelper().PostAttackLogic(this, types);
    }
    public void useBasic() {
        ArrayList<DamageType> types = new ArrayList<>();
        types.add(DamageType.BASIC);
        getBattle().getHelper().PreAttackLogic(this, types);

        AbstractEnemy enemy = getBattle().getMiddleEnemy();
        getBattle().getHelper().hitEnemy(this, enemy, 1.0f, BattleHelpers.MultiplierStat.ATK, types, TOUGHNESS_DAMAGE_SINGLE_UNIT);
        getBattle().getHelper().PostAttackLogic(this, types);
    }

    public void useUltimate() {
        isParrying = true;
        addPower(cullPower);

        tauntPower.lastsForever = true; // used so I can manually remove later
        for (AbstractEnemy enemy : getBattle().getEnemies()) {
            enemy.addPower(tauntPower);
        }
    }

    public void onAttacked(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> t, int energyFromAttacked, float totalDmg) {
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
            ArrayList<DamageType> types = new ArrayList<>();
            types.add(DamageType.FOLLOW_UP);
            increaseEnergy(5, "from Normal Counter");
            getBattle().getHelper().PreAttackLogic(this, types);

            int enemyIndex = getBattle().getEnemies().indexOf(enemy);
            getBattle().getHelper().hitEnemy(this, enemy, 1.2f, BattleHelpers.MultiplierStat.ATK, types, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            if (enemyIndex + 1 < getBattle().getEnemies().size()) {
                getBattle().getHelper().hitEnemy(this, getBattle().getEnemies().get(enemyIndex + 1), 0.6f, BattleHelpers.MultiplierStat.ATK, types, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            }
            if (enemyIndex - 1 >= 0) {
                getBattle().getHelper().hitEnemy(this, getBattle().getEnemies().get(enemyIndex - 1), 0.6f, BattleHelpers.MultiplierStat.ATK, types, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            }
            getBattle().getHelper().PostAttackLogic(this, types);
        }
        increaseEnergy(15, TALENT_ENERGY_GAIN);
        super.onAttacked(character, enemy, t, energyFromAttacked, totalDmg);
    }

    public void useCull(AbstractEnemy enemy) {
        increaseEnergy(10, "from using Cull");
        AbstractPower power = new DanceAtSunset.DanceAtSunsetDamagePower();
        AbstractPower sunsetPower = getPower(power.name);
        if (sunsetPower != null && sunsetPower.stacks == 2) {
            num2StackCulls++;
        } else {
            num1StackCulls++;
        }
        getBattle().addToLog(new UseCull(this));
        ArrayList<DamageType> types = new ArrayList<>();
        types.add(DamageType.FOLLOW_UP);
        types.add(DamageType.ULTIMATE);
        getBattle().getHelper().PreAttackLogic(this, types);

        int enemyIndex = getBattle().getEnemies().indexOf(enemy);
        getBattle().getHelper().hitEnemy(this, enemy, 2.2f, BattleHelpers.MultiplierStat.ATK, types, TOUGHNESS_DAMAGE_SINGLE_UNIT);
        if (enemyIndex + 1 < getBattle().getEnemies().size()) {
            getBattle().getHelper().hitEnemy(this, getBattle().getEnemies().get(enemyIndex + 1), 1.1f, BattleHelpers.MultiplierStat.ATK, types, TOUGHNESS_DAMAGE_SINGLE_UNIT);
        }
        if (enemyIndex - 1 >= 0) {
            getBattle().getHelper().hitEnemy(this, getBattle().getEnemies().get(enemyIndex - 1), 1.1f, BattleHelpers.MultiplierStat.ATK, types, TOUGHNESS_DAMAGE_SINGLE_UNIT);
        }

        int numBounces = 6;
        while (numBounces > 0) {
            getBattle().getHelper().hitEnemy(this, getBattle().getRandomEnemy(), 0.72f, BattleHelpers.MultiplierStat.ATK, types, TOUGHNESS_DAMAGE_SINGLE_UNIT / 4);
            numBounces--;
        }
        getBattle().getHelper().PostAttackLogic(this, types);
    }

    public void useSlash(AbstractEnemy enemy) {
        increaseEnergy(10, "from using Slash");
        numSlashes++;
        getBattle().addToLog(new UseSlash(this));
        ArrayList<DamageType> types = new ArrayList<>();
        types.add(DamageType.FOLLOW_UP);
        types.add(DamageType.ULTIMATE);
        getBattle().getHelper().PreAttackLogic(this, types);

        int enemyIndex = getBattle().getEnemies().indexOf(enemy);
        getBattle().getHelper().hitEnemy(this, enemy, 2.2f, BattleHelpers.MultiplierStat.ATK, types, TOUGHNESS_DAMAGE_SINGLE_UNIT);
        if (enemyIndex + 1 < getBattle().getEnemies().size()) {
            getBattle().getHelper().hitEnemy(this, getBattle().getEnemies().get(enemyIndex + 1), 1.1f, BattleHelpers.MultiplierStat.ATK, types, TOUGHNESS_DAMAGE_SINGLE_UNIT);
        }
        if (enemyIndex - 1 >= 0) {
            getBattle().getHelper().hitEnemy(this, getBattle().getEnemies().get(enemyIndex - 1), 1.1f, BattleHelpers.MultiplierStat.ATK, types, TOUGHNESS_DAMAGE_SINGLE_UNIT);
        }

        removePower(cullPower);
        isParrying = false;
        for (AbstractEnemy e : getBattle().getEnemies()) {
            e.removePower(tauntPower);
        }

        getBattle().getHelper().PostAttackLogic(this, types);
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
            this.name = this.getClass().getSimpleName();
            this.lastsForever = true;
        }

        @Override
        public float getConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.FOLLOW_UP) {
                    return 100;
                }
            }
            return 0;
        }
    }
}
