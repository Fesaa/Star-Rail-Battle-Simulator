package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class KnightOfPurityPalace extends AbstractRelicSetBonus {
    public KnightOfPurityPalace(AbstractCharacter<?> owner) {
        super(owner);
    }
    public KnightOfPurityPalace(AbstractCharacter<?> owner, boolean isFullSet) {
        super(owner, isFullSet);
    }

    public void onEquip() {
        owner.addPower(PermPower.create(PowerStat.DEF_PERCENT, 15, "Knight Defense Bonus"));
    }

    public String toString() {
        if (isFullSet) {
            return "4 PC Knight of Purity";
        } else {
            return "2 PC Knight of Purity";
        }
    }

}
