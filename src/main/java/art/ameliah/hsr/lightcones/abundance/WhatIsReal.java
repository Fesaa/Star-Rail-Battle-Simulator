package art.ameliah.hsr.lightcones.abundance;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

/**
 * Extra healing on basic is ignored, as there is no hook
 */
public class WhatIsReal extends AbstractLightcone {

    public WhatIsReal(AbstractCharacter<?> owner) {
        super(1058, 423, 331, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.BREAK_EFFECT, 48, "What Is Real Break Effect Boost"));
    }
}
