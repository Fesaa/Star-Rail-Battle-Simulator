package art.ameliah.hsr.lightcones.preservation;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;

public class WeAreWildfire extends AbstractLightcone {

    public WeAreWildfire(AbstractCharacter<?> owner) {
        super(741, 476, 463, owner);
    }

    @Override
    public void onCombatStart() {
        // TODO: DMG reduction & HP regen
    }


}
