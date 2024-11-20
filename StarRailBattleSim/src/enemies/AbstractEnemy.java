package enemies;

import battleLogic.AbstractEntity;
import battleLogic.log.lines.enemy.EnemyAction;
import battleLogic.log.lines.enemy.EnemyDied;
import battleLogic.log.lines.enemy.ForcedAttack;
import battleLogic.log.lines.enemy.ReduceToughness;
import battleLogic.log.lines.enemy.RuanMeiDelay;
import battleLogic.log.lines.enemy.SecondAction;
import battleLogic.log.lines.enemy.WeaknessBreakRecover;
import characters.AbstractCharacter;
import characters.moze.Moze;
import characters.ruanmei.RuanMei;
import powers.AbstractPower;
import powers.PowerStat;
import powers.TauntPower;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractEnemy extends AbstractEntity {

    public enum EnemyAttackType {
        AOE(25), BLAST(20), SINGLE(55);

        public final int weight;

        EnemyAttackType(int weight) {
            this.weight = weight;
        }
    }
    public int baseHP;
    public int baseAtk;
    public int baseDef;
    public int level;
    protected HashMap<AbstractCharacter.ElementType, Integer> resMap;
    public ArrayList<AbstractCharacter.ElementType> weaknessMap;
    public final int DEFAULT_RES = 20;
    public float toughness;
    public float maxToughness;
    public boolean weaknessBroken = false;
    public final int doubleActionCooldown;
    public int doubleActionCounter;
    public int numAttacksMetric = 0;
    public int numSingleTargetMetric = 0;
    public int numBlastMetric = 0;
    public int numAoEMetric = 0;
    public int timesBrokenMetric = 0;

    private float currentHP = 0;

    public AbstractEnemy(String name, int baseHP, int baseAtk, int baseDef, int baseSpeed, int level, int toughness, int doubleActionCooldown) {
        super();
        this.name = name;
        this.baseHP = baseHP;
        this.baseAtk = baseAtk;
        this.baseDef = baseDef;
        this.baseSpeed = baseSpeed;
        this.level = level;
        this.maxToughness = toughness;
        this.toughness = toughness;
        this.doubleActionCooldown = doubleActionCooldown;
        this.doubleActionCounter = doubleActionCooldown;
        powerList = new ArrayList<>();
        weaknessMap = new ArrayList<>();
        setUpDefaultRes();
    }

    public AbstractEnemy(String name, int baseHP, int baseAtk, int baseDef, int baseSpeed, int level, int toughness) {
        this(name, baseHP, baseAtk, baseDef, baseSpeed, level, toughness, -1);
    }

    public float getFinalAttack() {
        int totalBaseAtk = baseAtk;
        int totalBonusAtkPercent = 0;
        int totalBonusFlatAtk = 0;
        for (AbstractPower power : powerList) {
            totalBonusAtkPercent += power.getStat(PowerStat.ATK_PERCENT);
            totalBonusFlatAtk += power.getStat(PowerStat.FLAT_ATK);
        }
        return (int) (totalBaseAtk * (1 + totalBonusAtkPercent / 100) + totalBonusFlatAtk);
    }

    public float getFinalDefense() {
        float totalDefenseBonus = 0;
        float totalDefenseReduction = 0;
        for (AbstractPower power : powerList) {
            totalDefenseBonus += power.getStat(PowerStat.DEF_PERCENT);
            totalDefenseReduction += power.getStat(PowerStat.DEFENSE_REDUCTION);
        }
        return totalDefenseBonus - totalDefenseReduction;
    }
    public void setUpDefaultRes() {
        resMap = new HashMap<>();
        resMap.put(AbstractCharacter.ElementType.FIRE, DEFAULT_RES);
        resMap.put(AbstractCharacter.ElementType.ICE, DEFAULT_RES);
        resMap.put(AbstractCharacter.ElementType.WIND, DEFAULT_RES);
        resMap.put(AbstractCharacter.ElementType.LIGHTNING, DEFAULT_RES);
        resMap.put(AbstractCharacter.ElementType.PHYSICAL, DEFAULT_RES);
        resMap.put(AbstractCharacter.ElementType.QUANTUM, DEFAULT_RES);
        resMap.put(AbstractCharacter.ElementType.IMAGINARY, DEFAULT_RES);
    }

    public void setRes(AbstractCharacter.ElementType elementType, int resValue) {
        resMap.put(elementType, resValue);
    }

    public int getRes(AbstractCharacter.ElementType elementType) {
        return resMap.get(elementType);
    }

    public void takeTurn() {
        if (weaknessBroken) {
            return;
        }
        attack();
        if (doubleActionCounter == 0) {
            getBattle().addToLog(new SecondAction(this));
            attack();
            doubleActionCounter = doubleActionCooldown;
        } else {
            doubleActionCounter--;
        }
        numTurnsMetric++;
    }

    // TODO: Implement this correctly with buffs etc.
    protected float attackDmg() {
        return this.getFinalAttack();
    }

    protected int getRandomTargetPosition() {
        AbstractPower taunt = getPower(TauntPower.class.getSimpleName());

        if (taunt instanceof TauntPower) {
            AbstractCharacter<?> taunter = ((TauntPower) taunt).taunter;
            getBattle().addToLog(new ForcedAttack(this, taunter));
            for (int i = 0; i < getBattle().getPlayers().size(); i++) {
                if (getBattle().getPlayers().get(i) == taunter) {
                    return i;
                }
            }

            throw new RuntimeException("Taunter not present in battle.");
        }


        // This code included a correcting from the previous code.
        // It would use the position of the target inside validTargets
        // This would be the wrong position to use later on.
        // We use the new AbstractCharacter#canBeAttacked method to make sure a character can be attacked
        // Currently only Moze.

        double totalWeight = getBattle().getPlayers().stream().mapToDouble(AbstractCharacter::getFinalTauntValue).sum();
        int characterPosition = 0;
        // TODO: Check if it actually checks like this. I'd think that this way the first position gets attacked lot more
        // even with same taunt. As it just gets checked first. Not sure.

        double r = getBattle().getEnemyTargetRng().nextDouble() * totalWeight;
        for (int pos = 0; pos < getBattle().getPlayers().size(); pos++) {
            AbstractCharacter<?> character = getBattle().getPlayers().get(pos);
            if (!character.canBeAttacked()) {
                // Do not update r value if character cannot be attacked. See todo for concerns.
                continue;
            }

            r -= getBattle().getPlayers().get(pos).getFinalTauntValue();
            if (r <= 0) {
                return pos;
            }
        }

        // Attack last character is no character is found
        return getBattle().getPlayers().size() -1;
    }

    public void attack() {
        numAttacksMetric++;
        float dmgToDeal = this.attackDmg();

        EnemyAttackType attackType = rollAttackType();
        if (attackType == EnemyAttackType.AOE) {
            numAoEMetric++;
            getBattle().addToLog(new EnemyAction(this, null, EnemyAttackType.AOE));
            for (AbstractCharacter<?> character : getBattle().getPlayers()) {
                getBattle().getHelper().attackCharacter(this, character, 10, dmgToDeal);
            }
            return;
        }

        int targetPos = this.getRandomTargetPosition();
        AbstractCharacter<?> target = getBattle().getPlayers().get(targetPos); // Fine to throw if pos is too large.

        if (attackType == EnemyAttackType.SINGLE) {
            numSingleTargetMetric++;
            getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.SINGLE));
            getBattle().getHelper().attackCharacter(this, target, 10, dmgToDeal);
        } else {
            numBlastMetric++;
            getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.BLAST));
            getBattle().getHelper().attackCharacter(this, target, 10, dmgToDeal);
            if (targetPos + 1 < getBattle().getPlayers().size()) {
                getBattle().getHelper().attackCharacter(this, getBattle().getPlayers().get(targetPos + 1), 5, dmgToDeal);
            }
            if (targetPos - 1 >= 0) {
                getBattle().getHelper().attackCharacter(this, getBattle().getPlayers().get(targetPos - 1), 5,dmgToDeal );
            }
        }
    }

    public EnemyAttackType rollAttackType() {
        double totalWeight= 0.0;
        for (EnemyAttackType type : EnemyAttackType.values()) {
            totalWeight += type.weight;
        }
        int idx = 0;
        for (double r = getBattle().getEnemyMoveRng().nextDouble() * totalWeight; idx < EnemyAttackType.values().length - 1; ++idx) {
            r -= EnemyAttackType.values()[idx].weight;
            if (r <= 0.0) break;
        }
        return EnemyAttackType.values()[idx];
    }

    // TODO: Check with dark how this would work
    @Override
    public void onCombatStart() {
        this.currentHP = this.baseHP;
    }

    @Override
    public void onTurnStart() {
        if (this.weaknessBroken) {
            RuanMei.RuanMeiUltDebuff ruanMeiDebuff = (RuanMei.RuanMeiUltDebuff) this.getPower(RuanMei.ULT_DEBUFF_NAME);
            if (ruanMeiDebuff != null && !ruanMeiDebuff.triggered) {
                getBattle().addToLog(new RuanMeiDelay());
                float delay = ruanMeiDebuff.owner.getTotalBreakEffect() * 0.2f + 10;
                getBattle().DelayEntity(this, delay);
                ruanMeiDebuff.triggered = true;
                getBattle().getHelper().breakDamageHitEnemy(ruanMeiDebuff.owner, this, 0.5f);
            } else {
                if (this.hasPower(RuanMei.ULT_DEBUFF_NAME)) {
                    this.removePower(RuanMei.ULT_DEBUFF_NAME);
                }
                this.weaknessBroken = false;
                getBattle().addToLog(new WeaknessBreakRecover(this, this.toughness, this.maxToughness));
                this.toughness = maxToughness;
            }
        }
    }

    public void reduceToughness(float amount) {
        if (this.weaknessBroken) {
            return;
        }
        float initialToughness = this.toughness;
        this.toughness -= amount;
        if (this.toughness <= 0) {
            this.toughness = 0;
            this.weaknessBroken = true;
        }
        getBattle().addToLog(new ReduceToughness(this, amount, initialToughness, this.toughness));
        if (this.weaknessBroken) {
            timesBrokenMetric++;
            getBattle().DelayEntity(this, 25);
            for (AbstractCharacter character : getBattle().getPlayers()) {
                character.onWeaknessBreak(this);
            }
        }
    }

    @Override
    public void onAttacked(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<AbstractCharacter.DamageType> types, int energyFromAttacked, float totalDmg) {
        this.currentHP -= totalDmg;

        if (this.currentHP <= 0) {
            getBattle().removeEnemy(this);
            getBattle().addToLog(new EnemyDied(this, character));
        }
    }

    public String getMetrics() {
        return String.format("Metrics for %s with %d speed \nTurns taken: %d \nTotal attacks: %d \nSingle-target attacks: %d \nBlast attacks: %d \nAoE attacks: %d \nWeakness Broken: %d", name, baseSpeed, numTurnsMetric, numAttacksMetric, numSingleTargetMetric, numBlastMetric, numAoEMetric, timesBrokenMetric);
    }
}
