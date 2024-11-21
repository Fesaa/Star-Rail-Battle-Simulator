package art.ameliah.hsr.battleLogic.combat;

import art.ameliah.hsr.battleLogic.Battle;
import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.march.SwordMarch;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PowerStat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class Hit implements BattleParticipant {

    @Getter
    private final AbstractCharacter<?> source;
    @Getter
    private final AbstractEnemy target;
    private final float multiplier;
    private final MultiplierStat stat;
    @Getter
    private final List<DamageType> types;
    private final float toughnessDmg;
    private final ElementType attackElement;
    private final boolean ignoreWeakness;

    public float finalToughnessReduction() {
        float weaknessBreakEff = this.source.getTotalWeaknessBreakEff();
        float toughnessDmg = this.toughnessDmg * (1 + weaknessBreakEff / 100);

        // March should pass true ignore weakness after checking herself
        if (this.target.hasWeakness(this.attackElement) || this.ignoreWeakness) {
            return toughnessDmg;
        }

        return 0;
    }

    public float finalDmg() {
        float baseDamage = this.baseDamage();
        float critMultiplier = this.critMultiplier();
        float dmgBoostMultiplier = this.dmgBoostMultiplier();
        // float weaken = this.weakenMultiplier()
        float defMultiplier = this.defenceMultiplier();
        float resMultiplier = this.resMultiplier();
        float vulnerabilityMultiplier = this.vulnerabilityMultiplier();
        //float dmgMitigationMultiplier = this.dmgMitigationMultiplier();
        float brokenMultiplier = this.brokenMultiplier();

        float calculatedDmg = baseDamage
                * critMultiplier
                * dmgBoostMultiplier
                // * weaken
                * defMultiplier
                * resMultiplier
                * vulnerabilityMultiplier
                // * dmgMitigationMultiplier
                * brokenMultiplier;
        return calculatedDmg;
    }

    private float brokenMultiplier() {
        return this.target.isWeaknessBroken() ? 1 : 0.9f;
    }

    private float vulnerabilityMultiplier() {
        //float elementVulnerability = 0; // TODO: Add element dmg taken
        float allTypeVulnerability = 0;
        //float dotVulnerability = 0; // TODO: Add dot

        for (var power : this.source.powerList) {
            allTypeVulnerability += power.getStat(PowerStat.DAMAGE_TAKEN);
            allTypeVulnerability += power.getConditionalDamageTaken(this.source, this.target, types);
        }

        return 1 + allTypeVulnerability/100;
    }

    private float resMultiplier() {
        float resPen = 0;

        for (var power : this.source.powerList) {
            resPen += power.getStat(PowerStat.RES_PEN);
        }

        return 1 - (this.target.getRes(this.attackElement)-resPen)/100;
    }

    // Currently doesn't care for enemy def buffs.
    // I'm not sure how these should work
    private float defenceMultiplier() {
        float defReduction = 0;
        float defIgnore = 0;

        for (var power : this.target.powerList) {
            defReduction += power.getStat(PowerStat.DEFENSE_REDUCTION);
            defIgnore += power.getStat(PowerStat.DEFENSE_IGNORE);
        }

        for (var power : this.source.powerList) {
            defIgnore += power.getConditionDefenseIgnore(this.source, this.target, this.types);
        }

        float nominator = (this.target.getLevel() + 20) * (1 - defReduction/100-defIgnore/100) + this.source.level+20;
        return (this.source.level + 20) / nominator;
    }

    private float dmgBoostMultiplier() {
        float dmgMultiplier = 0;

        for (var power : this.source.powerList) {
            dmgMultiplier += power.getStat(this.attackElement.getStatBoost());
            dmgMultiplier += power.getStat(PowerStat.DAMAGE_BONUS);
            dmgMultiplier += power.getConditionalDamageBonus(this.source, this.target, this.types);
        }

        for (var power : this.target.powerList) {
            dmgMultiplier += power.receiveConditionalDamageBonus(this.source, this.target, this.types);
        }

        return 1 + dmgMultiplier / 100;
    }

    private float critMultiplier() {
        float critChance = this.critChance();
        float critDamage = this.critDamage();

        float expectedCritMultiplier = (100.0f + critDamage * critChance * 0.01f) / 100;
        float critMultiplier = (100.0f + critDamage) / 100;
        return expectedCritMultiplier;
    }

    private float critChance() {
        float critChance = this.source.getTotalCritChance();

        for (var power : this.source.powerList) {
            critChance += power.getConditionalCritRate(this.source, this.target, this.types);
        }
        for (var power : this.source.powerList) {
            critChance = power.setFixedCritRate(this.source, this.target, this.types, critChance);
        }

        return critChance;
    }

    private float critDamage() {
        float critDamage = this.source.getTotalCritDamage();

        for (var power : this.source.powerList) {
            critDamage += power.getConditionalCritDamage(this.source, this.target, this.types);
        }
        for (var power : this.target.powerList) {
            critDamage += power.receiveConditionalCritDamage(this.source, this.target, this.types);
        }
        for (var power : this.source.powerList) {
            critDamage = power.setFixedCritDmg(this.source, this.target, this.types, critDamage);
        }

        return critDamage;
    }

    private float baseDamage() {
        return switch (this.stat) {
            case ATK -> this.source.getFinalAttack();
            case HP -> this.source.getFinalHP();
            case DEF -> this.source.getFinalDefense();
        } * this.multiplier;
    }

    @Override
    public IBattle getBattle() {
        return this.source.getBattle();
    }
}
