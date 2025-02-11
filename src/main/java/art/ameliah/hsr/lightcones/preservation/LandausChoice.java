package art.ameliah.hsr.lightcones.preservation;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class LandausChoice extends AbstractLightcone {

    public LandausChoice(AbstractCharacter<?> owner) {
        super(953, 423, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.TAUNT_VALUE, 200, "Landau's Choice Taunt Value Boost"));
        // Dunno if this work
        this.owner.addPower(PermPower.create(PowerStat.DAMAGE_TAKEN, -24, "Landau's Choice Damage Taken Reduction"));
    }
}
