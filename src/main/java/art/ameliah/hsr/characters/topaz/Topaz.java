package art.ameliah.hsr.characters.topaz;

import art.ameliah.hsr.battleLogic.AbstractSummon;
import art.ameliah.hsr.battleLogic.BattleHelpers;
import art.ameliah.hsr.battleLogic.Numby;
import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.AbstractSummoner;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.AlwaysSkillGoal;
import art.ameliah.hsr.characters.goal.shared.AlwaysUltGoal;
import art.ameliah.hsr.characters.goal.shared.DontUltMissingPowerGoal;
import art.ameliah.hsr.characters.goal.shared.SkillFirstTurnGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TracePower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Topaz extends AbstractSummoner<Topaz> implements SkillFirstTurnGoal.FirstTurnTracked {
    final AbstractPower proofOfDebt = new ProofOfDebt();
    final Numby numby;
    PermPower stonksPower;
    int ultCounter = 0;
    private boolean techniqueActive = false;

    public int numbyAttacksMetrics = 0;
    public int numbyAdvancedTimesMetrics = 0;
    public int actualNumbyAdvanceMetric = 0;
    public int wastedNumbyAdvances = 0;
    private final String numbyAttacksMetricName = "Numby Attacks";
    private final String numbyAdvancedTimesMetricName = "Number of Useful Numby Advances";
    private final String actualNumbyAdvanceMetricName = "Amount of AV Advanced by Numby";
    private final String wastedNumbyAdvancesMetricName = "Number of Wasted Numby Advances";
    private final String leftoverAVNumbyMetricName = "Leftover AV (Numby)";
    private final String leftoverUltChargesMetricName = "Leftover Ult Charges";
    public static final String NAME = "Topaz";

    public Topaz() {
        super(NAME, 931, 621, 412, 110, 80, ElementType.FIRE, 130, 75, Path.HUNT);

        this.addPower(new TracePower()
                .setStat(PowerStat.FIRE_DMG_BOOST, 22.4f)
                .setStat(PowerStat.CRIT_CHANCE, 12.0f)
                .setStat(PowerStat.HP_PERCENT, 10));

        numby = new Numby(this);
        stonksPower = new PermPower();

        this.registerGoal(0, DontUltMissingPowerGoal.robin(this));
        this.registerGoal(10, new TopazUltGoal(this));
        this.registerGoal(20, new AlwaysUltGoal<>(this));

        this.registerGoal(0, new SkillFirstTurnGoal<>(this));
        this.registerGoal(10, new TopazTurnGoal(this));
        this.registerGoal(20, new AlwaysSkillGoal<>(this));
    }

    public void useSkill() {
        for (AbstractEnemy enemy : getBattle().getEnemies()) {
            if (enemy.hasPower(proofOfDebt.name)) {
                enemy.removePower(proofOfDebt);
                break;
            }
        }

        AbstractEnemy target = getBattle().getEnemyWithHighestHP();
        target.addPower(proofOfDebt);

        numbyAttack(DamageType.SKILL);
    }
    public void useBasic() {
        AbstractEnemy target = null;
        for (AbstractEnemy enemy : getBattle().getEnemies()) {
            if (enemy.hasPower(proofOfDebt.name)) {
                target = enemy;
                break;
            }
        }

        if (target == null) {
            throw new IllegalStateException("No enemy found with proofOfDebt");
        }

        this.startAttack()
                .hitEnemy(target, 1.0f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.BASIC, DamageType.FOLLOW_UP)
                .execute();
    }

    public void useUltimate() {
        this.stonksPower = PermPower.create(PowerStat.CRIT_DAMAGE, 25, "Topaz Ult Power");
        ultCounter = 2;
        this.addPower(this.stonksPower);
    }

    public void onCombatStart() {
        this.addPower(new FireWeaknessBonusDamage());
        getBattle().getActionValueMap().put(numby, numby.getBaseAV());
        getBattle().getRandomEnemy().addPower(proofOfDebt);
    }

    public void numbyAttack(DamageType ...types) {
        this.numbyAttack(new ArrayList<>(List.of(types)));
    }

    public void numbyAttack(List<DamageType> types) {
        float multiplier;
        float toughnessDamage;
        if (ultCounter > 0) {
            multiplier = 2.1f;
            toughnessDamage = TOUGHNESS_DAMAGE_TWO_UNITS / 8;
        } else {
            multiplier = 1.5f;
            toughnessDamage = TOUGHNESS_DAMAGE_TWO_UNITS / 7;
        }
        types.add(DamageType.FOLLOW_UP);

        AbstractEnemy target = null;
        for (AbstractEnemy enemy : getBattle().getEnemies()) {
            if (enemy.hasPower(proofOfDebt.name)) {
                target = enemy;
                break;
            }
        }

        if (target == null) {
            throw new IllegalStateException("No targets with Proof of Debt");
        }

        Attack attack = this.startAttack();

        for (int i = 0; i < 7; i++) {
            attack.hitEnemy(target, multiplier/7, MultiplierStat.ATK, toughnessDamage, types);
        }
        if (ultCounter > 0) {
            attack.hitEnemy(target, 0.9f, MultiplierStat.ATK, toughnessDamage, types);
            increaseEnergy(10, "from Enhanced Numby attack");
            ultCounter--;
            if (ultCounter <= 0) {
                if (types.contains(DamageType.SKILL)) {
                    numby.AdvanceForward(); //manually advance numby when topaz skills with last charge of ult
                }
                removePower(stonksPower);
            }
        }

        if (techniqueActive) {
            techniqueActive = false;
            increaseEnergy(60, TECHNIQUE_ENERGY_GAIN);
        }

        attack.execute();
    }

    public void useTechnique() {
        techniqueActive = true;
    }

    public HashMap<String, String> getCharacterSpecificMetricMap() {
        HashMap<String, String> map = super.getCharacterSpecificMetricMap();
        map.put(leftoverAVNumbyMetricName, String.valueOf(getBattle().getActionValueMap().get(numby)));
        map.put(leftoverUltChargesMetricName, String.valueOf(ultCounter));
        map.put(numbyAttacksMetricName, String.valueOf(numbyAttacksMetrics));
        map.put(numbyAdvancedTimesMetricName, String.valueOf(numbyAdvancedTimesMetrics));
        map.put(actualNumbyAdvanceMetricName, String.valueOf(actualNumbyAdvanceMetric));
        map.put(wastedNumbyAdvancesMetricName, String.valueOf(wastedNumbyAdvances));
        return map;
    }

    public ArrayList<String> getOrderedCharacterSpecificMetricsKeys() {
        ArrayList<String> list = super.getOrderedCharacterSpecificMetricsKeys();
        list.add(leftoverAVNumbyMetricName);
        list.add(leftoverUltChargesMetricName);
        list.add(numbyAttacksMetricName);
        list.add(numbyAdvancedTimesMetricName);
        list.add(actualNumbyAdvanceMetricName);
        list.add(wastedNumbyAdvancesMetricName);
        return list;
    }

    @Override
    public List<AbstractSummon<Topaz>> getSummons() {
        return Collections.singletonList(numby);
    }

    @Override
    public boolean isFirstTurn() {
        return firstMove;
    }

    @Override
    public void setFirstTurn(boolean firstTurn) {
        firstMove = firstTurn;
    }

    private class ProofOfDebt extends AbstractPower {
        public ProofOfDebt() {
            this.name = this.getClass().getSimpleName();
            lastsForever = true;
            this.type = PowerType.DEBUFF;
        }

        @Override
        public void onDeath() {
            getBattle().getRandomEnemy().addPower(this);
        }

        @Override
        public float getConditionalDamageTaken(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.FOLLOW_UP) {
                    return 50;
                }
            }
            return 0;
        }

        @Override
        public void onAttacked(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> types, int energyFromAttacked, float totalDmg) {
            for (DamageType type : types) {
                if (type == DamageType.FOLLOW_UP) {
                    Topaz.this.numby.AdvanceForward();
                    break;
                }
            }
            if (ultCounter > 0) {
                for (DamageType type : types) {
                    if (type == DamageType.BASIC || type == DamageType.SKILL || type == DamageType.ULTIMATE) {
                        Topaz.this.numby.AdvanceForward();
                        break;
                    }
                }
            }
        }
    }

    private static class FireWeaknessBonusDamage extends AbstractPower {
        public FireWeaknessBonusDamage() {
            this.name = this.getClass().getSimpleName();
            lastsForever = true;
        }

        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (enemy.hasWeakness(ElementType.FIRE)) {
                return 15;
            }
            return 0;
        }
    }
}
