package art.ameliah.hsr.lightcones.harmony;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class ForTomorrowsJourney extends AbstractLightcone {

    public ForTomorrowsJourney(AbstractCharacter<?> owner) {
        super(953, 476, 331, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.ATK_PERCENT, 32, "For Tomorrow's Journey ATK Boost"));
    }

    @Override
    public void onUseUltimate() {
        this.owner.addPower(TempPower.create(PowerStat.DAMAGE_BONUS, 30, 1, "For Tomorrow's Journey Damage Boost"));
    }
}
