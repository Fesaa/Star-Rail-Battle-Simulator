package art.ameliah.hsr.lightcones.abundance;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

/**
 * Healing on basic, or skill is ignored as there is no hook
 */
public class WarmthShortensColdNights extends AbstractLightcone {

    public WarmthShortensColdNights(AbstractCharacter<?> owner) {
        super(1058, 370, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.HP_PERCENT, 32, "Warmth Shortens Cold Nights HP Boost"));
    }
}
