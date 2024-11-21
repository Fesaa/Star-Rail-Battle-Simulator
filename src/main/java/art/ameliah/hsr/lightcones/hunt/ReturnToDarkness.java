package art.ameliah.hsr.lightcones.hunt;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class ReturnToDarkness extends AbstractLightcone {

    public ReturnToDarkness(AbstractCharacter<?> owner) {
        super(847, 529, 331, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_CHANCE, 24, "Return to Darkness Crit Chance Boost"));
    }
}

