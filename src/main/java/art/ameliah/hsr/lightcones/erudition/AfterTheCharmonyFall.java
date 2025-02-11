package art.ameliah.hsr.lightcones.erudition;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class AfterTheCharmonyFall extends AbstractLightcone {

    public AfterTheCharmonyFall(AbstractCharacter<?> owner) {
        super(847, 476, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.BREAK_EFFECT, 56, "After the Charmony Fall Break Effect Boost"));
    }

    @Override
    public void onUseUltimate() {
        getBattle().IncreaseSpeed(this.owner, TempPower.create(PowerStat.SPEED_PERCENT, 16, 2, "After the Charmony Fall Speed Boost"));
    }
}
