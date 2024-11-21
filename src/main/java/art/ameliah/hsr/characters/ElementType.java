package art.ameliah.hsr.characters;

import art.ameliah.hsr.powers.PowerStat;
import lombok.Getter;

@Getter
public enum ElementType {
    FIRE(PowerStat.FIRE_DMG_BOOST),
    ICE(PowerStat.ICE_DMG_BOOST),
    WIND(PowerStat.WIND_DMG_BOOST),
    LIGHTNING(PowerStat.LIGHTNING_DMG_BOOST),
    PHYSICAL(PowerStat.PHYSICAL_DMG_BOOST),
    QUANTUM(PowerStat.QUANTUM_DMG_BOOST),
    IMAGINARY(PowerStat.IMAGINARY_DMG_BOOST);

    private final PowerStat statBoost;

    ElementType(PowerStat statBoost) {
        this.statBoost = statBoost;
    }
}
