package art.ameliah.hsr.lightcones.harmony;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class PlanetaryRendezvous extends AbstractLightcone {

    public PlanetaryRendezvous(AbstractCharacter<?> owner) {
        super(1058, 423, 331, owner);
    }

    @Override
    public void onCombatStart() {
        getBattle().getPlayers().stream()
                .filter(c -> c.elementType.equals(this.owner.elementType))
                .forEach(c -> c.addPower(PermPower.create(PowerStat.DAMAGE_BONUS, 24, "Planetary Rendezvous DMG Boost")));
    }
}
