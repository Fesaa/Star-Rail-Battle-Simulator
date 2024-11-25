package art.ameliah.hsr.characters.moze;

import art.ameliah.hsr.battleLogic.combat.AttackLogic;
import art.ameliah.hsr.battleLogic.log.lines.character.DoMove;
import art.ameliah.hsr.battleLogic.log.lines.entity.GainCharge;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysSkillGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.characters.goal.shared.ult.DontUltMissingPowerGoal;
import art.ameliah.hsr.characters.goal.shared.ult.UltAtEndOfBattle;
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
    private final String FUAsMetricName = "Number of Follow Up Attacks Used";
    private final String talentProcsMetricName = "Talent Extra Damage Procs";
    private final MozePreyPower preyPower;
    private final int MAX_CHARGE = 9;
    private final int CHARGE_ATTACK_THRESHOLD = 3;
    public int FUAs = 0;
    public int talentProcs = 0;
    public boolean isDeparted = false;
    private int chargeCount;
    private int chargeLost = 0;
    private boolean skillPointRecovered = false;

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
        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));
    }

    @Override
    public void useSkill() {
        this.startAttack().handle(DamageType.SKILL, dh -> dh.logic(this.getTarget(MoveType.SKILL), (e, al) -> {
            this.preyPower.setOwner(e);
            e.addPower(this.preyPower);
            this.increaseCharge(MAX_CHARGE);
            this.isDeparted = true;

            al.hit(e, 1.65f, TOUGHNESS_DAMAGE_TWO_UNITS);
        })).afterAttackHook(() -> getBattle().getActionValueMap().remove(this)).execute();
    }

    @Override
    public void useBasic() {
        this.doAttack(DamageType.BASIC, MoveType.BASIC, (e, al) -> {
            al.hit(e, 1.1f, TOUGHNESS_DAMAGE_SINGLE_UNIT);
        });
    }

    public void useUltimate() {
        this.addPower(TempPower.create(PowerStat.DAMAGE_BONUS, 30, 2, "Moze Damage Bonus"));
        this.startAttack().handle(dh -> {
            dh.addTypes(DamageType.ULTIMATE, DamageType.FOLLOW_UP);
            AbstractEnemy target = this.getTarget(MoveType.ULTIMATE);

            dh.logic(target, (e, al) -> {
                al.hit(e, 2.92f, TOUGHNESS_DAMAGE_THREE_UNITs);
            });
            this.useFollowUp(target);
        }).execute();
    }

    public void useFollowUp(AbstractEnemy enemy) {
        moveHistory.add(MoveType.FOLLOW_UP);
        FUAs++;
        getBattle().addToLog(new DoMove(this, MoveType.FOLLOW_UP));
        increaseEnergy(10, FUA_ENERGY_GAIN);

        float totalMult = 2.01f;
        float initialHitsMult = totalMult * 0.08f;
        float finalHitMult = totalMult * 0.6f;

        this.startAttack().handle(DamageType.FOLLOW_UP, dh -> {
            AbstractEnemy target = enemy.isDead() ? getBattle().getRandomEnemy() : enemy;
            dh.logic(target, al -> {
                for (int i = 0; i < 5; i++) {
                    al.hit(target, initialHitsMult);
                }
                al.hit(target, finalHitMult, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            });
        }).afterAttackHook(() -> {
            if (!skillPointRecovered) {
                getBattle().generateSkillPoint(this, 1);
                skillPointRecovered = true;
            }
        }).execute();
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
                useFollowUp((AbstractEnemy) preyPower.getOwner());
            }
        }

        if (chargeCount == 0) {
            preyPower.getOwner().removePower(preyPower);
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
            this.setName(this.getClass().getSimpleName());
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
        public void beforeAttacked(AttackLogic attack) {
            AbstractEnemy enemy = (AbstractEnemy) this.getOwner();

            boolean trigger = true;
            if (attack.getSource() instanceof Moze) {
                if ((attack.getTypes().contains(DamageType.SKILL) || attack.getTypes().contains(DamageType.FOLLOW_UP))) {
                    trigger = false;
                }
            }
            if (trigger) {
                talentProcs++;

                attack.hit(Moze.this, enemy, 0.33f);
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
