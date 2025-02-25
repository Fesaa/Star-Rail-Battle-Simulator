package art.ameliah.hsr.lightcones.preservation;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreUltimate;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class ThisIsMe extends AbstractLightcone {

    public ThisIsMe(AbstractCharacter<?> owner) {
        super(847, 370, 529, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.DEF_PERCENT, 32, "This Is Me Defense Boost"));
    }

    @Subscribe
    public void onUseUltimate(PreUltimate e) {
        // I'm not 100% sure this is correct.
        // This effect only applies 1 time per enemy target during each use of the wearer's Ultimate.
        TempPower dmgBoost = TempPower.create(PowerStat.DAMAGE_BONUS, (float) (this.owner.getFinalDefense() * 1.2), 1, "This Is Me Damage Boost");
        dmgBoost.justApplied = false;
        this.owner.addPower(dmgBoost);
    }
}
