package art.ameliah.hsr.lightcones.abundance;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

/**
 * The extra damage from healing allies is ignored
 */
public class TimeWaitsForNoOne extends AbstractLightcone {

    public TimeWaitsForNoOne(AbstractCharacter<?> owner) {
        super(1270, 476, 463, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.HP_PERCENT, 18, "Time Waits For No One HP Boost"));
        this.owner.addPower(PermPower.create(PowerStat.HEALING, 12, "Time Waits For No One Healing Boost"));
    }
}
