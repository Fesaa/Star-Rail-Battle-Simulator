package art.ameliah.hsr.characters.feixiao;

import art.ameliah.hsr.battleLogic.BattleHelpers;
import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.battleLogic.log.lines.character.DoMove;
import art.ameliah.hsr.battleLogic.log.lines.character.GainEnergy;
import art.ameliah.hsr.battleLogic.log.lines.character.lingsha.FuYuanLose;
import art.ameliah.hsr.battleLogic.log.lines.entity.GainCharge;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.AlwaysSkillGoal;
import art.ameliah.hsr.characters.goal.shared.AlwaysUltGoal;
import art.ameliah.hsr.characters.goal.shared.BroynaRobinUltGoal;
import art.ameliah.hsr.characters.goal.shared.DontUltMissingDebuffGoal;
import art.ameliah.hsr.characters.goal.shared.DontUltMissingPowerGoal;
import art.ameliah.hsr.characters.goal.shared.UltAtEndOfBattle;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Feixiao extends AbstractCharacter<Feixiao> {

    private Random fuaRng;

    final PermPower ultBreakEffBuff = PermPower.create(PowerStat.WEAKNESS_BREAK_EFF, 100, "Fei Ult Break Eff Buff");
    private int numFUAs = 0;
    private int numStacks;
    private int wastedStacks;
    private final String numFUAsMetricName = "Follow up Attacks used";
    private final String numStacksMetricName = "Amount of Talent Stacks gained";
    private final String wastedStacksMetricName = "Amount of overcapped Stacks";
    public int stackCount = 0;
    public final int stackThreshold = 2;
    private boolean FUAReady = true;
    public static final String NAME = "Feixiao";

    public Feixiao() {
        super(NAME, 1048, 602, 388, 112, 80, ElementType.WIND, 12, 75, Path.HUNT);

        this.usesEnergy = false;
        this.currentEnergy = 0;
        this.ultCost = 6;
        this.isDPS = true;
        this.hasAttackingUltimate = true;

        this.addPower(new TracePower()
                .setStat(PowerStat.ATK_PERCENT, 28)
                .setStat(PowerStat.CRIT_CHANCE, 12)
                .setStat(PowerStat.DEF_PERCENT, 12.5f));

        this.registerGoal(0, new FeixiaoBronyaTurnGoal(this));
        this.registerGoal(10, new AlwaysSkillGoal<>(this, 1));

        this.registerGoal(0, new UltAtEndOfBattle<>(this));
        this.registerGoal(10, new BroynaRobinUltGoal<>(this));
        this.registerGoal(20, DontUltMissingPowerGoal.robin(this));
        this.registerGoal(30, DontUltMissingPowerGoal.sparkle(this));
        this.registerGoal(40, DontUltMissingPowerGoal.bronya(this));
        this.registerGoal(50, DontUltMissingPowerGoal.ruanmei(this));
        this.registerGoal(60, DontUltMissingPowerGoal.hanya(this));
        this.registerGoal(70, DontUltMissingDebuffGoal.pela(this));
        this.registerGoal(100, new AlwaysUltGoal<>(this));
    }


    public void increaseStack(int amount) {
        int initialStack = stackCount;
        stackCount += amount;
        getBattle().addToLog(new GainCharge(this, amount, initialStack, stackCount, "Stack"));
        if (stackCount >= stackThreshold) {
            int energyGain = stackCount / stackThreshold;
            gainStackEnergy(energyGain);
        }
    }

    public void gainStackEnergy(int energyGain) {
        numStacks += energyGain;
        float initialEnergy = currentEnergy;
        currentEnergy += energyGain;
        if (currentEnergy > maxEnergy) {
            currentEnergy = maxEnergy;
            wastedStacks += energyGain;
        }
        stackCount = stackCount % stackThreshold;
        getBattle().addToLog(new GainEnergy(this, initialEnergy, this.currentEnergy, energyGain, TALENT_ENERGY_GAIN));
    }

    public void useSkill() {
        this.addPower(TempPower.create(PowerStat.ATK_PERCENT, 48, 3,"Fei Atk Bonus"));

        Attack attack = this.startAttack();
        AbstractEnemy enemy = getBattle().getEnemyWithHighestHP();

        float totalMult = 2.0f;
        attack.hitEnemy(enemy, totalMult * 0.34f, MultiplierStat.ATK, 0, DamageType.SKILL);
        attack.hitEnemy(enemy, totalMult * 0.33f, MultiplierStat.ATK, 0, DamageType.SKILL);
        attack.hitEnemy(enemy, totalMult * 0.33f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_TWO_UNITS, DamageType.SKILL);
        attack.execute();

        this.useFollowUp(enemy);
    }
    public void useBasic() {
        this.startAttack()
                .hitEnemy(getBattle().getEnemyWithHighestHP(), 1, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.BASIC)
                .execute();
    }

    public void useFollowUp(AbstractEnemy target) {
        if (target.isDead()) {
            target = getBattle().getRandomEnemy();
        }

        moveHistory.add(MoveType.FOLLOW_UP);
        numFUAs++;
        getBattle().addToLog(new DoMove(this, MoveType.FOLLOW_UP));

        this.addPower(TempPower.create(PowerStat.DAMAGE_BONUS, 60, 2, "Fei Damage Bonus"));

        this.startAttack()
                .hitEnemy(target, 1.1f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_HALF_UNIT, DamageType.FOLLOW_UP)
                .execute();
    }

    public void useUltimate() {
        AbstractEnemy enemy = getBattle().getEnemyWithHighestHP();

        this.addPower(ultBreakEffBuff);

        int numHits = 6;
        Attack attack = this.startAttack();

        float totalMult = 0.9f;
        for (int i = 0; i < numHits; i++) {
            if (enemy.isWeaknessBroken()) {
                attack.hitEnemy(enemy, totalMult * 0.1f, MultiplierStat.ATK, 0, DamageType.ULTIMATE, DamageType.FOLLOW_UP);
                attack.hitEnemy(enemy, totalMult * 0.9f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_HALF_UNIT, DamageType.ULTIMATE, DamageType.FOLLOW_UP);
            } else {
                attack.hitEnemy(enemy, totalMult, MultiplierStat.ATK, TOUGHNESS_DAMAGE_HALF_UNIT, DamageType.ULTIMATE, DamageType.FOLLOW_UP);
            }
        }

        attack.hitEnemy(enemy, 1.6f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_HALF_UNIT, DamageType.ULTIMATE, DamageType.FOLLOW_UP);

        attack.execute();

        this.removePower(ultBreakEffBuff);
    }

    public void onTurnStart() {
        if (currentEnergy >= ultCost) {
            tryUltimate(); // check for ultimate activation at start of turn as well
        }
        if (FUAReady) {
            increaseStack(1);
        }
        FUAReady = true;
    }

    public void onCombatStart() {
        this.fuaRng = new Random(getBattle().getSeed());
        gainStackEnergy(3);
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            AbstractPower feiPower = new FeiTalentPower();
            character.addPower(feiPower);
        }
        addPower(new FeiCritDmgPower());
    }

    public void useTechnique() {
        if (getBattle().usedEntryTechnique()) {
            return;
        } else {
            getBattle().setUsedEntryTechnique(true);
        }
        // This assumes a fix multiplier on her technique
        this.startAttack()
                .hitEnemies(getBattle().getEnemies(), 2, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT)
                .execute();
        this.gainStackEnergy(1);
    }

    public HashMap<String, String> getCharacterSpecificMetricMap() {
        HashMap<String, String> map = super.getCharacterSpecificMetricMap();
        map.put(numFUAsMetricName, String.valueOf(numFUAs));
        map.put(numStacksMetricName, String.valueOf(numStacks));
        map.put(wastedStacksMetricName, String.valueOf(wastedStacks));
        return map;
    }

    public ArrayList<String> getOrderedCharacterSpecificMetricsKeys() {
        ArrayList<String> list = super.getOrderedCharacterSpecificMetricsKeys();
        list.add(numFUAsMetricName);
        list.add(numStacksMetricName);
        list.add(wastedStacksMetricName);
        return list;
    }

    public HashMap<String, String> addLeftoverCharacterEnergyMetric(HashMap<String, String> metricMap) {
        metricMap.put(leftoverEnergyMetricName, String.format("%.2f (Flying Aureus)", this.currentEnergy));
        return metricMap;
    }

    private class FeiTalentPower extends AbstractPower {
        public FeiTalentPower() {
            this.name = this.getClass().getSimpleName();
            this.lastsForever = true;
        }

        @Override
        public void onAttack(Attack attack) {
            if (!Feixiao.this.hasPower(ultBreakEffBuff.name)) {
                Feixiao.this.increaseStack(1);
            }
        }
        @Override
        public void afterAttackFinish(AbstractCharacter<?> character, Set<AbstractEnemy> enemiesHit, List<DamageType> types) {
            if (!(character instanceof Feixiao)) {
                if (FUAReady) {
                    FUAReady = false;

                    List<AbstractEnemy> alive = enemiesHit.stream().filter(e -> !e.isDead()).toList();
                    AbstractEnemy enemy;
                    if (alive.isEmpty()) {
                        enemy = getBattle().getRandomEnemy();
                    } else {
                        enemy = alive.get(Feixiao.this.fuaRng.nextInt(alive.size()));
                    }
                    Feixiao.this.useFollowUp(enemy);
                }
            }
        }
    }

    private static class FeiCritDmgPower extends AbstractPower {
        public FeiCritDmgPower() {
            this.name = this.getClass().getSimpleName();
            this.lastsForever = true;
        }

        @Override
        public float getConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.FOLLOW_UP) {
                    return 36;
                }
            }
            return 0;
        }
    }
}
