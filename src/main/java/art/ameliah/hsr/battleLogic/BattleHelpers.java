package art.ameliah.hsr.battleLogic;

import art.ameliah.hsr.battleLogic.log.lines.character.Attacked;
import art.ameliah.hsr.battleLogic.log.lines.character.BreakDamageHitResult;
import art.ameliah.hsr.battleLogic.log.lines.character.CritHitResult;
import art.ameliah.hsr.battleLogic.log.lines.character.TotalDamage;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.march.SwordMarch;
import art.ameliah.hsr.characters.moze.Moze;
import art.ameliah.hsr.enemies.AbstractEnemy;
import java.util.ArrayList;
import java.util.HashMap;

import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PowerStat;

public class BattleHelpers implements BattleParticipant {

    private final IBattle battle;

    public BattleHelpers(IBattle battle) {
        this.battle = battle;
    }

    @Override
    public IBattle getBattle() {
        return this.battle;
    }

    @Override
    public boolean inBattle() {
        return BattleParticipant.super.inBattle();
    }

    public enum MultiplierStat {
        ATK, HP, DEF
    }
    
    private float attackDamageTotal = 0;
    public ArrayList<AbstractEnemy> enemiesHit = new ArrayList<>();
    private final HashMap<String, Float> damageBonusMultiConstituents = new HashMap<>();
    private final HashMap<String, Float> defenseMultiConstituents = new HashMap<>();
    private final HashMap<String, Float> resMultiConstituents = new HashMap<>();
    private final HashMap<String, Float> damageVulnMultiConstituents = new HashMap<>();
    private final HashMap<String, Float> critDmgMultiConstituents = new HashMap<>();

    private void clearConstituents() {
        damageBonusMultiConstituents.clear();
        defenseMultiConstituents.clear();
        resMultiConstituents.clear();
        damageVulnMultiConstituents.clear();
        critDmgMultiConstituents.clear();
    }
    
    public float calculateDamageAgainstEnemy(AbstractCharacter<?> source, AbstractEnemy target, float multiplier, MultiplierStat stat, ArrayList<DamageType> types, ElementType damageElement) {
        clearConstituents();
        float statToUse;
        if (stat == MultiplierStat.ATK) {
            statToUse = source.getFinalAttack();
        } else if (stat == MultiplierStat.HP) {
            statToUse = source.getFinalHP();
        } else {
            statToUse = source.getFinalDefense();
        }
        float baseDamage = multiplier * statToUse;

        float dmgMultiplier = 0;
        if (damageElement == source.elementType) {
            for (AbstractPower power : source.powerList) {
                float sameBonus = power.getStat(PowerStat.SAME_ELEMENT_DAMAGE_BONUS);
                float globalBonus = power.getStat(PowerStat.DAMAGE_BONUS);
                dmgMultiplier += sameBonus;
                dmgMultiplier += globalBonus;
                if (sameBonus != 0) {
                    damageBonusMultiConstituents.put(power.name, sameBonus);
                }
                if (globalBonus != 0) {
                    damageBonusMultiConstituents.put(power.name, globalBonus);
                }
            }
        } else {
            for (AbstractPower power : source.powerList) {
                float bonus = power.getStat(PowerStat.DAMAGE_BONUS);
                dmgMultiplier += bonus;
                if (bonus != 0) {
                    damageBonusMultiConstituents.put(power.name, bonus);
                }
            }
        }
        for (AbstractPower power : source.powerList) {
            float bonus = power.getConditionalDamageBonus(source, target, types);
            dmgMultiplier += bonus;
            if (bonus != 0) {
                damageBonusMultiConstituents.put(power.name, bonus);
            }
        }
        for (AbstractPower power : target.powerList) {
            float bonus = power.receiveConditionalDamageBonus(source, target, types);
            dmgMultiplier += bonus;
            if (bonus != 0) {
                damageBonusMultiConstituents.put(power.name, bonus);
            }
        }
        float dmgMultiplierFloat = 1 + dmgMultiplier / 100;

        float enemyDefPercent = 0;
        for (AbstractPower power : target.powerList) {
            float bonus = power.getStat(PowerStat.DEF_PERCENT);
            float reduction = power.getStat(PowerStat.DEFENSE_REDUCTION);
            enemyDefPercent += bonus;
            enemyDefPercent -= reduction;
            if (bonus != 0) {
                defenseMultiConstituents.put(power.name, bonus);
            }
            if (reduction != 0) {
                defenseMultiConstituents.put(power.name, -reduction);
            }
        }
        for (AbstractPower power : source.powerList) {
            float ignore = power.getConditionDefenseIgnore(source, target, types);
            enemyDefPercent -= ignore;
            if (ignore != 0) {
                defenseMultiConstituents.put(power.name, -ignore);
            }
        }
        if (enemyDefPercent < -100) {
            enemyDefPercent = -100;
        }
        float defMultiplierFloat = (source.level + 20) / ((target.getLevel() + 20) * (1 + enemyDefPercent / 100) + (source.level + 20));

        float resPen = 0;
        for (AbstractPower power : source.powerList) {
            float bonus = power.getStat(PowerStat.RES_PEN);
            resPen += bonus;
            if (bonus != 0) {
                resMultiConstituents.put(power.name, bonus);
            }
        }
        if (target.getRes(damageElement) != 0) {
            resMultiConstituents.put("Enemy Res", (float)-target.getRes(damageElement));
        }
        float resMultiplier = 100 + (-target.getRes(damageElement) + resPen);
        float resMultiplierFloat = resMultiplier / 100;

        float damageTaken = 0;
        for (AbstractPower power : target.powerList) {
            float constBonus = power.getStat(PowerStat.DAMAGE_TAKEN);
            float condBonus = power.getConditionalDamageTaken(source, target, types);
            damageTaken += constBonus;
            damageTaken += condBonus;
            if (constBonus != 0) {
                damageVulnMultiConstituents.put(power.name, constBonus);
            }
            if (condBonus != 0) {
                damageVulnMultiConstituents.put(power.name, condBonus);
            }
        }
        float damageTakenMultiplier = 1 + damageTaken / 100;

        float toughnessMultiplier = 0.9f;
        if (target.isWeaknessBroken()) {
            toughnessMultiplier = 1.0f;
        }

        float critChance = source.getTotalCritChance();
        for (AbstractPower power : source.powerList) {
            critChance += power.getConditionalCritRate(source, target, types);
        }
        for (AbstractPower power : source.powerList) {
            critChance = power.setFixedCritRate(source, target, types, critChance);
        }

        float critDamage = source.baseCritDamage;
        for (AbstractPower power : source.powerList) {
            float bonus = power.getStat(PowerStat.CRIT_DAMAGE);
            critDamage += bonus;
            if (bonus != 0) {
                critDmgMultiConstituents.put(power.name, bonus);
            }
        }
        for (AbstractPower power : source.powerList) {
            float bonus = power.getConditionalCritDamage(source, target, types);
            critDamage += bonus;
            if (bonus != 0) {
                critDmgMultiConstituents.put(power.name, bonus);
            }
        }
        for (AbstractPower power : target.powerList) {
            float bonus = power.receiveConditionalCritDamage(source, target, types);
            critDamage += bonus;
            if (bonus != 0) {
                critDmgMultiConstituents.put(power.name, bonus);
            }
        }
        for (AbstractPower power : source.powerList) {
            float fixed = power.setFixedCritDmg(source, target, types, critDamage);
            if (fixed != critDamage) {
                critDmgMultiConstituents.clear();
                critDmgMultiConstituents.put(power.name, fixed);
            }
            critDamage = fixed;
        }
        float expectedCritMultiplier = (100.0f + critDamage * critChance * 0.01f) / 100;
        float critMultiplier = (100.0f + critDamage) / 100;

        float calculatedDamage = baseDamage * dmgMultiplierFloat * defMultiplierFloat * resMultiplierFloat * damageTakenMultiplier * toughnessMultiplier * expectedCritMultiplier;
        getBattle().addToLog(new CritHitResult(source, target, calculatedDamage, baseDamage, dmgMultiplierFloat, defMultiplierFloat, resMultiplierFloat, damageTakenMultiplier, toughnessMultiplier, critMultiplier, expectedCritMultiplier,
                damageBonusMultiConstituents, defenseMultiConstituents, resMultiConstituents, damageVulnMultiConstituents, critDmgMultiConstituents));
        return calculatedDamage;
    }

    public float calculateBreakDamageAgainstEnemy(AbstractCharacter<?> source, AbstractEnemy target, float multiplier, ElementType damageElement) {
        float maxToughnessMultiplier = 0.5f + (target.maxToughness() / 40);
        float baseDamage = this.getBaseBreakDamage(multiplier, damageElement, maxToughnessMultiplier);
        float breakEffectMultiplier = source.getTotalBreakEffect();
        float breakEffectMultiplierFloat = 1 + breakEffectMultiplier / 100;

        float enemyDefPercent = target.getFinalDefense();
        float defMultiplierFloat = (source.level + 20) / ((target.getLevel() + 20) * (1 + enemyDefPercent / 100) + (source.level + 20));

        float resPen = source.getTotalResPen();
        float resMultiplier = 100 - (target.getRes(damageElement) - resPen);
        float resMultiplierFloat = resMultiplier / 100;

        ArrayList<DamageType> types = new ArrayList<>();
        types.add(DamageType.BREAK);
        float damageTaken = 0;
        for (AbstractPower power : target.powerList) {
            damageTaken += power.getStat(PowerStat.DAMAGE_TAKEN);
            damageTaken += power.getConditionalDamageTaken(source, target, types);
        }
        float damageTakenMultiplier = 1 + damageTaken / 100;

        float toughnessMultiplier = 0.9f;
        if (target.isWeaknessBroken()) {
            toughnessMultiplier = 1.0f;
        }

        float calculatedDamage = baseDamage * breakEffectMultiplierFloat * defMultiplierFloat * resMultiplierFloat * damageTakenMultiplier * toughnessMultiplier;
        getBattle().addToLog(new BreakDamageHitResult(source, target, calculatedDamage, baseDamage, breakEffectMultiplierFloat, defMultiplierFloat, resMultiplierFloat, damageTakenMultiplier, toughnessMultiplier));
        return calculatedDamage;
    }

    private float getBaseBreakDamage(float multiplier, ElementType damageElement, float maxToughnessMultiplier) {
        float elementMultipler;
        if (damageElement == ElementType.ICE || damageElement == ElementType.LIGHTNING) {
            elementMultipler = 1;
        } else if (damageElement == ElementType.PHYSICAL || damageElement == ElementType.FIRE) {
            elementMultipler = 2;
        } else if (damageElement == ElementType.QUANTUM || damageElement == ElementType.IMAGINARY) {
            elementMultipler = 0.5f;
        } else {
            elementMultipler = 1.5f;
        }
        return multiplier * elementMultipler * 3767.5533f * maxToughnessMultiplier;
    }

    public float calculateToughenssDamage(AbstractCharacter<?> character, float toughnssDamage) {
        float weaknessBreakEff = character.getTotalWeaknessBreakEff();
        return toughnssDamage * (1 + weaknessBreakEff / 100);
    }

    public void hitEnemy(AbstractCharacter<?> source, AbstractEnemy target, float multiplier, MultiplierStat stat, ArrayList<DamageType> types, float toughnessDamage, ElementType damageElement) {
        source.emit(l -> l.onBeforeHitEnemy(source, target, types));
        target.emit(l -> l.onBeforeHitEnemy(source, target, types));
        float calculatedDamage = calculateDamageAgainstEnemy(source, target, multiplier, stat, types, damageElement);

        toughnessDamage = calculateToughenssDamage(source, toughnessDamage);
        if (target.hasWeakness(damageElement) && toughnessDamage > 0) {
            target.reduceToughness(toughnessDamage);
        } else {
            if (source instanceof SwordMarch) {
                if (damageElement == source.elementType && ((SwordMarch) source).master != null && target.hasWeakness(((SwordMarch) source).master.elementType)) {
                    target.reduceToughness(toughnessDamage);
                }
            }
        }
        getBattle().increaseTotalPlayerDmg(calculatedDamage);
        getBattle().updateContribution(source, calculatedDamage);
        attackDamageTotal += calculatedDamage;
        if (!enemiesHit.contains(target)) {
            enemiesHit.add(target);
        }
    }

    public void hitEnemy(AbstractCharacter<?> source, AbstractEnemy target, float multiplier, MultiplierStat stat, ArrayList<DamageType> types, float toughnessDamage) {
        hitEnemy(source, target, multiplier, stat, types, toughnessDamage, source.elementType);
    }

    public void PreAttackLogic(AbstractCharacter<?> character, ArrayList<DamageType> types) {
        attackDamageTotal = 0;
        enemiesHit.clear();
        character.emit(l -> l.onBeforeUseAttack(types));
    }

    public void PostAttackLogic(AbstractCharacter<?> character, ArrayList<DamageType> types) {
        int damageTotal = (int) attackDamageTotal;
        getBattle().addToLog(new TotalDamage(character, types, damageTotal));
        getBattle().addToLog(new Attacked(character, enemiesHit));

        character.emit(l -> l.onAttack(character, enemiesHit, types));
        ArrayList<AbstractEnemy> enemies = new ArrayList<>(enemiesHit); // I really should've implemented an action queue
        for (AbstractEnemy enemy : enemies) {
            enemy.emit(l -> l.onAttacked(character, enemy, types, 0, damageTotal));
        }
        character.emit(l -> l.afterAttackFinish(character, enemiesHit, types));
    }

    public void attackCharacter(AbstractEnemy source, AbstractCharacter<?> target, int energyToGain, float dmg) {
        if (target instanceof Moze) {
            if (((Moze) target).isDeparted) {
                return;
            }
        }
        getBattle().addToLog(new Attacked(source, target));
        target.emit(l -> l.onAttacked(target, source, new ArrayList<>(), energyToGain, dmg));
    }

    public void additionalDamageHitEnemy(AbstractCharacter<?> source, AbstractEnemy target, float multiplier, MultiplierStat stat) {
        float calculatedDamage = calculateDamageAgainstEnemy(source, target, multiplier, stat, new ArrayList<>(), source.elementType);
        getBattle().increaseTotalPlayerDmg(calculatedDamage);
        getBattle().updateContribution(source, calculatedDamage);
        attackDamageTotal += calculatedDamage;
    }

    public void tingyunSkillHitEnemy(AbstractCharacter<?> source, AbstractEnemy target, float multiplier, MultiplierStat stat) {
        float calculatedDamage = calculateDamageAgainstEnemy(source, target, multiplier, stat, new ArrayList<>(),  ElementType.LIGHTNING);
        getBattle().increaseTotalPlayerDmg(calculatedDamage);
        getBattle().updateContribution(source, calculatedDamage);
        attackDamageTotal += calculatedDamage;
    }
    public void breakDamageHitEnemy(AbstractCharacter<?> source, AbstractEnemy target, float multiplier) {
        float calculatedDamage = calculateBreakDamageAgainstEnemy(source, target, multiplier, source.elementType);
        getBattle().increaseTotalPlayerDmg(calculatedDamage);
        getBattle().updateContribution(source, calculatedDamage);
        attackDamageTotal += calculatedDamage;
    }
}
