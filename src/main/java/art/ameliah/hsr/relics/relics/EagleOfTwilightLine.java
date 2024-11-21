package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class EagleOfTwilightLine extends AbstractRelicSetBonus {
    public EagleOfTwilightLine(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public EagleOfTwilightLine(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.WIND_DMG_BOOST, 10, "Eagle Of Twilight Line Wind bonus"));
    }

    @Override
    public void onUseUltimate() {
        if (!this.isFullSet) return;

        getBattle().AdvanceEntity(this.owner, 25);
    }
}
