package art.ameliah.hsr.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.ArrayList;
import java.util.HashMap;

public class RelicStats {

    private final HashMap<Stats, Float> mainStatValues = new HashMap<>();
    private final HashMap<Stats, Float> subStatValues = new HashMap<>();

    private final ArrayList<Stats> usedMainStats = new ArrayList<>();
    private final HashMap<Stats, Integer> usedSubStats = new HashMap<>();

    public RelicStats() {
        initializeValues();
        this.addMainStat(Stats.HP_FLAT).addMainStat(Stats.ATK_FLAT);
    }

    public void equipTo(AbstractCharacter<?> character) {
        PermPower relicBonus = new PermPower();

        relicBonus.setStat(PowerStat.FLAT_HP, getTotalBonus(Stats.HP_FLAT));
        relicBonus.setStat(PowerStat.FLAT_ATK, getTotalBonus(Stats.ATK_FLAT));
        relicBonus.setStat(PowerStat.FLAT_DEF, getTotalBonus(Stats.DEF_FLAT));
        relicBonus.setStat(PowerStat.HP_PERCENT, getTotalBonus(Stats.HP_PER));
        relicBonus.setStat(PowerStat.ATK_PERCENT, getTotalBonus(Stats.ATK_PER));
        relicBonus.setStat(PowerStat.DEF_PERCENT, getTotalBonus(Stats.DEF_PER));
        relicBonus.setStat(PowerStat.CRIT_CHANCE, getTotalBonus(Stats.CRIT_RATE));
        relicBonus.setStat(PowerStat.CRIT_DAMAGE, getTotalBonus(Stats.CRIT_DAMAGE));
        relicBonus.setStat(PowerStat.EFFECT_HIT, getTotalBonus(Stats.EFFECT_HIT));
        relicBonus.setStat(PowerStat.EFFECT_RES, getTotalBonus(Stats.EFFECT_RES));
        relicBonus.setStat(PowerStat.BREAK_EFFECT, getTotalBonus(Stats.BREAK_EFFECT));
        relicBonus.setStat(PowerStat.FLAT_SPEED, getTotalBonus(Stats.SPEED));
        relicBonus.setStat(PowerStat.HEALING, getTotalBonus(Stats.HEALING));
        relicBonus.setStat(PowerStat.ENERGY_REGEN, getTotalBonus(Stats.ERR));
        
        // Assuming the ELEMENT_DMG is always correct for the char
        Stats convStat = this.fromElementType(character.elementType);
        relicBonus.setStat(fromRelicStat(convStat), getTotalBonus(Stats.ELEMENT_DAMAGE));
        relicBonus.setName("RelicStatsBonuses");
        character.addPower(relicBonus);
    }

    private Stats fromElementType(ElementType type) {
        return switch (type) {
            case FIRE -> Stats.FIRE_DAMAGE;
            case ICE -> Stats.ICE_DAMAGE;
            case WIND -> Stats.WIND_DAMAGE;
            case LIGHTNING -> Stats.LIGHTNING_DAMAGE;
            case PHYSICAL -> Stats.PHYSICAL_DAMAGE;
            case QUANTUM -> Stats.QUANTUM_DAMAGE;
            case IMAGINARY -> Stats.IMAGINARY_DAMAGE;
        };
    }

    private PowerStat fromRelicStat(Stats stat) {
        return switch (stat) {
            case ERR -> PowerStat.ENERGY_REGEN;
            case BREAK_EFFECT -> PowerStat.BREAK_EFFECT;
            case SPEED -> PowerStat.FLAT_SPEED;
            case HEALING -> PowerStat.HEALING;
            case HP_PER -> PowerStat.HP_PERCENT;
            case ATK_PER -> PowerStat.ATK_PERCENT;
            case DEF_PER -> PowerStat.DEF_PERCENT;
            case HP_FLAT -> PowerStat.FLAT_HP;
            case ATK_FLAT -> PowerStat.FLAT_ATK;
            case DEF_FLAT -> PowerStat.FLAT_DEF;
            case CRIT_RATE -> PowerStat.CRIT_CHANCE;
            case CRIT_DAMAGE -> PowerStat.CRIT_DAMAGE;
            case EFFECT_HIT -> PowerStat.EFFECT_HIT;
            case EFFECT_RES -> PowerStat.EFFECT_RES;
            case ICE_DAMAGE -> PowerStat.ICE_DMG_BOOST;
            case ELEMENT_DAMAGE -> throw new IllegalStateException("This shouldn't be happening?");
            case PHYSICAL_DAMAGE -> PowerStat.PHYSICAL_DMG_BOOST;
            case FIRE_DAMAGE -> PowerStat.FIRE_DMG_BOOST;
            case LIGHTNING_DAMAGE -> PowerStat.LIGHTNING_DMG_BOOST;
            case WIND_DAMAGE -> PowerStat.WIND_DMG_BOOST;
            case QUANTUM_DAMAGE -> PowerStat.QUANTUM_DMG_BOOST;
            case IMAGINARY_DAMAGE -> PowerStat.IMAGINARY_DMG_BOOST;
        };
    }

    public RelicStats addMainStat(Stats stat) {
        usedMainStats.add(stat);
        return this;
    }

    public RelicStats addSubStat(Stats stat, int numRolls) {
        usedSubStats.put(stat, numRolls);
        return this;
    }

    private float getTotalBonus(Stats stat) {
        float total = 0;
        for (Stats mainStat : usedMainStats) {
            if (mainStat == stat) {
                total += mainStatValues.get(stat);
            }
        }
        if (usedSubStats.containsKey(stat)) {
            total += subStatValues.get(stat) * usedSubStats.get(stat);
        }
        return total;
    }

    private void initializeValues() {
        mainStatValues.put(Stats.HP_FLAT, 705.0f);
        mainStatValues.put(Stats.ATK_FLAT, 352.0f);
        mainStatValues.put(Stats.HP_PER, 43.2f);
        mainStatValues.put(Stats.ATK_PER, 43.2f);
        mainStatValues.put(Stats.DEF_PER, 54.0f);
        mainStatValues.put(Stats.CRIT_RATE, 32.4f);
        mainStatValues.put(Stats.CRIT_DAMAGE, 64.8f);
        mainStatValues.put(Stats.EFFECT_HIT, 43.2f);
        mainStatValues.put(Stats.BREAK_EFFECT, 64.8f);
        mainStatValues.put(Stats.SPEED, 25.0f);
        mainStatValues.put(Stats.HEALING, 34.5f);
        mainStatValues.put(Stats.ERR, 19.4f);
        mainStatValues.put(Stats.ELEMENT_DAMAGE, 38.8f);

        subStatValues.put(Stats.HP_FLAT, 38.0f);
        subStatValues.put(Stats.ATK_FLAT, 19.0f);
        subStatValues.put(Stats.DEF_FLAT, 19.0f);
        subStatValues.put(Stats.HP_PER, 3.8f);
        subStatValues.put(Stats.ATK_PER, 3.8f);
        subStatValues.put(Stats.DEF_PER, 4.8f);
        subStatValues.put(Stats.CRIT_RATE, 2.9f);
        subStatValues.put(Stats.CRIT_DAMAGE, 5.8f);
        subStatValues.put(Stats.EFFECT_HIT, 3.8f);
        subStatValues.put(Stats.EFFECT_RES, 3.8f);
        subStatValues.put(Stats.BREAK_EFFECT, 5.8f);
        subStatValues.put(Stats.SPEED, 2.3f);
    }

}
