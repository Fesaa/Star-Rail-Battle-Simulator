package art.ameliah.hsr.lightcones.abundance;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostAllyAttack;
import art.ameliah.hsr.lightcones.AbstractLightcone;

public class Multiplication extends AbstractLightcone {

    public Multiplication(AbstractCharacter<?> owner) {
        super(953, 318, 198, owner);
    }

    @Subscribe
    public void afterAttack(PostAllyAttack e) {
        if (e.getAttack().getTypes().contains(DamageType.BASIC)) {
            getBattle().AdvanceEntity(e.getAttack().getSource(), 20);
        }
    }

}
