package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;


public class GuardInTheWutheringSnow extends AbstractRelicSetBonus {
    public GuardInTheWutheringSnow(AbstractCharacter<?> owner) {
        super(owner);


    }

    public GuardInTheWutheringSnow(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);


    }

    @Override
    public void onTurnStart() {
        // TODO: Regen HP

        //if (this.owner.getCurrentHP() < this.owner.getFinalHP() /2 ) {
        //    this.owner.increaseEnergy(5);
        //}
    }
}
