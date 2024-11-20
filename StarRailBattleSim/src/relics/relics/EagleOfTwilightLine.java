package relics.relics;

import characters.AbstractCharacter;
import characters.ElementType;
import powers.PermPower;
import powers.PowerStat;
import relics.AbstractRelicSetBonus;

public class EagleOfTwilightLine extends AbstractRelicSetBonus {
    public EagleOfTwilightLine(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public EagleOfTwilightLine(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        if (this.owner.elementType == ElementType.WIND) {
            this.owner.addPower(PermPower.create(PowerStat.SAME_ELEMENT_DAMAGE_BONUS, 10, "Eagle Of Twilight Line Wind bonus"));
        }
    }

    @Override
    public void onUseUltimate() {
        if (!this.isFullSet) return;

        getBattle().AdvanceEntity(this.owner, 25);
    }
}
