package art.ameliah.hsr.lightcones.harmony;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreUltimate;
import art.ameliah.hsr.lightcones.AbstractLightcone;

public class DanceDanceDance extends AbstractLightcone {

    public DanceDanceDance(AbstractCharacter<?> owner) {
        super(953, 423, 397, owner);
    }

    @Subscribe
    public void onUseUltimate(PreUltimate e) {
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            getBattle().AdvanceEntity(character, 24);
        }
    }
}
