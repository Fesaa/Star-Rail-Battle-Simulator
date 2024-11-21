package art.ameliah.hsr.lightcones.destruction;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class NowhereToRun extends AbstractLightcone {
    public NowhereToRun(AbstractCharacter<?> owner) {
        super(953, 529, 265, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.ATK_PERCENT, 48, "Nowhere to Run ATK Boost"));
    }
}
