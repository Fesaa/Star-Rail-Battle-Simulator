package art.ameliah.hsr.characters.remembrance.aglaea;

import art.ameliah.hsr.battleLogic.AbstractEntity;

public class SupremeStanceEntity extends AbstractEntity {

    private final Aglaea aglaea;

    public SupremeStanceEntity(Aglaea aglaea) {
        this.aglaea = aglaea;
        this.baseSpeed = 100;
    }

    @Override
    public void takeTurn() {
        Garmentmaker garmentmaker = (Garmentmaker) this.aglaea.getMemo();

        if (garmentmaker != null) {
            getBattle().removeEntity(garmentmaker);
            garmentmaker.onDeath();
        }

        getBattle().removeEntity(this);
        this.aglaea.getSupremeStance().set(false);
        this.aglaea.removePower(Aglaea.DanceDestinedWeaveress.NAME);
    }
}
