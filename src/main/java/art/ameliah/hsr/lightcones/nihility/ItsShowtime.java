package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

/**
 * Don't think there are hooks for inflicting debugs yet
 */
public class ItsShowtime extends AbstractLightcone {

    public ItsShowtime(AbstractCharacter<?> owner) {
        super(1058, 476, 265, owner);

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent e) {
        if (this.owner.getTotalEHR() > 80) {
            this.owner.addPower(PermPower.create(PowerStat.ATK_PERCENT, 36, "It's Showtime ATK Boost"));
        }
    }
}
