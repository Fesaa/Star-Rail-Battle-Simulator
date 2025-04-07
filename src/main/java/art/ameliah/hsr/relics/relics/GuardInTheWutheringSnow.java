package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.TurnStartEvent;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;


public class GuardInTheWutheringSnow extends AbstractRelicSetBonus {
    public GuardInTheWutheringSnow(AbstractCharacter<?> owner) {
        super(owner);


    }

    public GuardInTheWutheringSnow(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);


    }

    @Subscribe
    public void onTurnStart(TurnStartEvent event) {
        if (this.owner.getCurrentHp().get() < this.owner.getFinalHP() * 0.5f) {
            this.owner.increaseEnergy(5, "Guard of Wuthering Snow");
            this.owner.increaseHealth(this, this.owner.getFinalHP() * 0.08f);
        }
    }
}
