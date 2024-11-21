package art.ameliah.hsr.characters.moze;

import art.ameliah.hsr.battleLogic.BattleHelpers;
import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.battleLogic.log.lines.character.DoMove;
import art.ameliah.hsr.battleLogic.log.lines.entity.GainCharge;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.AlwaysSkillGoal;
import art.ameliah.hsr.characters.goal.shared.AlwaysUltGoal;
import art.ameliah.hsr.characters.goal.shared.DontUltMissingPowerGoal;
import art.ameliah.hsr.characters.goal.shared.UltAtEndOfBattle;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Moze extends AbstractCharacter<Moze> {
    public static final String NAME = "Moze";
    
    public int FUAs = 0;
    public int talentProcs = 0;
    private final String FUAsMetricName = "Number of Follow Up Attacks Used";
    private final String talentProcsMetricName = "Talent Extra Damage Procs";
    private final MozePreyPower preyPower;
    private int chargeCount;
    private final int MAX_CHARGE = 9;
    private int chargeLost = 0;
    private final int CHARGE_ATTACK_THRESHOLD = 3;
    private boolean skillPointRecovered = false;
    public boolean isDeparted = false;

    public Moze() {
        super(NAME, 811, 600, 353, 111, 80, ElementType.LIGHTNING, 120, 75, Path.HUNT);

        this.addPower(new TracePower()
                .setStat(PowerStat.ATK_PERCENT, 18)
                .setStat(PowerStat.CRIT_DAMAGE, 37.3f)
                .setStat(PowerStat.HP_PERCENT, 10));
        this.hasAttackingUltimate = true;

        preyPower = new MozePreyPower();

        this.registerGoal(0, new UltAtEndOfBattle<>(this));
        this.registerGoal(10, DontUltMissingPowerGoal.robin(this));
        this.registerGoal(20, new AlwaysUltGoal<>(this));
        this.registerGoal(0, new AlwaysSkillGoal<>(this));
    }

    @Override
    public void useSkill() {

        AbstractEnemy enemy = getBattle().getEnemyWithHighestHP();
        preyPower.owner = enemy;
        enemy.addPower(preyPower);
        increaseCharge(MAX_CHARGE);
        isDeparted = true;

        this.startAttack()
                .hitEnemy(enemy, 1.65f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_TWO_UNITS, DamageType.SKILL)
                .execute();
        getBattle().getActionValueMap().remove(this);
    }

    @Override
    public void useBasic() {
        this.startAttack()
                .hitEnemy(getBattle().getEnemyWithHighestHP(), 1.1f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.BASIC)
                .execute();
    }

    public void useUltimate() {
        this.addPower(TempPower.create(PowerStat.DAMAGE_BONUS, 30, 2, "Moze Damage Bonus"));
        this.startAttack()
                .hitEnemy(getBattle().getEnemyWithHighestHP(), 2.92f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_THREE_UNITs, DamageType.ULTIMATE, DamageType.FOLLOW_UP)
                .execute();

        useFollowUp(getBattle().getEnemyWithHighestHP());
    }

    public void useFollowUp(AbstractEnemy enemy) {
        moveHistory.add(MoveType.FOLLOW_UP);
        FUAs++;
        getBattle().addToLog(new DoMove(this, MoveType.FOLLOW_UP));
        increaseEnergy(10, FUA_ENERGY_GAIN);

        float totalMult = 2.01f;
        float initialHitsMult = totalMult * 0.08f;
        float finalHitMult = totalMult * 0.6f;

        Attack attack = this.startAttack();
        for (int i = 0; i < 5; i++) {
            attack.hitEnemy(enemy, initialHitsMult, MultiplierStat.ATK, 0, DamageType.FOLLOW_UP);
        }
        attack.hitEnemy(enemy, finalHitMult, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.FOLLOW_UP);
        attack.execute();

        if (!skillPointRecovered) {
            getBattle().generateSkillPoint(this, 1);
            skillPointRecovered = true;
        }
    }

    public void onTurnStart() {
        skillPointRecovered = false;
    }

    public void increaseCharge(int amount) {
        chargeLost = 0;
        int initalStack = chargeCount;
        chargeCount += amount;
        if (chargeCount > MAX_CHARGE) {
            chargeCount = MAX_CHARGE;
        }
        getBattle().addToLog(new GainCharge(this, amount, initalStack, chargeCount));
    }

    public void decreaseCharge(int amount) {
        if (chargeCount >= 1) {
            int initalStack = chargeCount;
            chargeCount -= amount;
            chargeLost += amount;
            if (chargeCount < 0) {
                chargeCount = 0;
            }
            getBattle().addToLog(new GainCharge(this, amount, initalStack, chargeCount));
            if (chargeLost >= CHARGE_ATTACK_THRESHOLD) {
                chargeLost -= CHARGE_ATTACK_THRESHOLD;
                useFollowUp((AbstractEnemy) preyPower.owner);
            }
        }

        if (chargeCount == 0) {
            preyPower.owner.removePower(preyPower);
        }
    }

    public void onCombatStart() {
        getBattle().AdvanceEntity(this, 30);
        increaseEnergy(20, "from E1");
    }

    @Override
    public boolean canBeAttacked() {
        return !this.isDeparted;
    }

    public HashMap<String, String> getCharacterSpecificMetricMap() {
        HashMap<String, String> map = super.getCharacterSpecificMetricMap();
        map.put(FUAsMetricName, String.valueOf(FUAs));
        map.put(talentProcsMetricName, String.valueOf(talentProcs));
        return map;
    }

    public ArrayList<String> getOrderedCharacterSpecificMetricsKeys() {
        ArrayList<String> list = super.getOrderedCharacterSpecificMetricsKeys();
        list.add(FUAsMetricName);
        list.add(talentProcsMetricName);
        return list;
    }

    public HashMap<String, String> addLeftoverCharacterAVMetric(HashMap<String, String> metricMap) {
        Float leftoverAV = getBattle().getActionValueMap().get(this);
        if (leftoverAV == null) {
            metricMap.put(leftoverAVMetricName, String.format("%,d (Charge Left)", chargeCount));
        } else {
            return super.addLeftoverCharacterAVMetric(metricMap);
        }

        return metricMap;
    }

    private class MozePreyPower extends AbstractPower {
        public MozePreyPower() {
            this.name = this.getClass().getSimpleName();
            this.type = PowerType.DEBUFF;
            this.lastsForever = true;
        }
        @Override
        public float receiveConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            return 40;
        }

        @Override
        public float getConditionalDamageTaken(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.FOLLOW_UP) {
                    return 25;
                }
            }
            return 0;
        }

        @Override
        public void beforeAttacked(Attack attack) {
            AbstractEnemy enemy = (AbstractEnemy) this.owner;

            boolean trigger = true;
            if (attack.getSource() instanceof Moze) {
                if ((attack.getTypes().contains(DamageType.SKILL) || attack.getTypes().contains(DamageType.FOLLOW_UP))) {
                    trigger = false;
                }
            }
            if (trigger) {
                talentProcs++;

                attack.hitEnemy(Moze.this, enemy, 0.33f, MultiplierStat.ATK);
                increaseEnergy(2, TALENT_ENERGY_GAIN);
                decreaseCharge(1);
            }
        }

        @Override
        public void onRemove() {
            getBattle().getActionValueMap().put(Moze.this, Moze.this.getBaseAV());
            getBattle().AdvanceEntity(Moze.this, 20);
            isDeparted = false;
        }
    }
}
