package art.ameliah.hsr.characters;

import art.ameliah.hsr.powers.PowerStat;
import lombok.Getter;

@Getter
public enum ElementType {
    FIRE(PowerStat.FIRE_DMG_BOOST, 0),
    ICE(PowerStat.ICE_DMG_BOOST, 0),
    WIND(PowerStat.WIND_DMG_BOOST, 0),
    LIGHTNING(PowerStat.LIGHTNING_DMG_BOOST, 0),
    PHYSICAL(PowerStat.PHYSICAL_DMG_BOOST, 0),
    QUANTUM(PowerStat.QUANTUM_DMG_BOOST, 20),
    IMAGINARY(PowerStat.IMAGINARY_DMG_BOOST, 30);

    private final PowerStat statBoost;
    private final float extraDelay;

    ElementType(PowerStat statBoost, float extraDelay) {
        this.statBoost = statBoost;
        this.extraDelay = extraDelay;
    }
}
