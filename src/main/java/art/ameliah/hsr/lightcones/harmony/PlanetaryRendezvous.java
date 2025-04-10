package art.ameliah.hsr.lightcones.harmony;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class PlanetaryRendezvous extends AbstractLightcone {

    public PlanetaryRendezvous(AbstractCharacter<?> owner) {
        super(1058, 423, 331, owner);
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        getBattle().registerForPlayers(p -> {
            if (p.elementType != this.owner.elementType) {
                return;
            }

            p.addPower(PermPower.create(PowerStat.DAMAGE_BONUS, 24, "Planetary Rendezvous DMG Boost"));
        });
    }
}
