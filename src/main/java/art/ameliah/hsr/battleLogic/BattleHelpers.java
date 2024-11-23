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
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattleHelpers implements BattleParticipant {

    private final IBattle battle;

    public BattleHelpers(IBattle battle) {
        this.battle = battle;
    }

    @Override
    public IBattle getBattle() {
        return this.battle;
    }

    private enum MultiplierStat {
        ATK, HP, DEF
    }

    private float calculateBreakDamageAgainstEnemy(AbstractCharacter<?> source, AbstractEnemy target, float multiplier, ElementType damageElement) {
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

}
