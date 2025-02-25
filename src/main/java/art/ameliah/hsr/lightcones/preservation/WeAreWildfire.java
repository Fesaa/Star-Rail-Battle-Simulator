package art.ameliah.hsr.lightcones.preservation;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.lightcones.AbstractLightcone;

public class WeAreWildfire extends AbstractLightcone {

    public WeAreWildfire(AbstractCharacter<?> owner) {
        super(741, 476, 463, owner);
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        // TODO: DMG reduction & HP regen
    }


}
