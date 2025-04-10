package art.ameliah.hsr.lightcones.harmony;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class EarthlyEscapade extends AbstractLightcone {
    private final boolean mask;

    public EarthlyEscapade(AbstractCharacter<?> owner) {
        this(owner, true);
    }

    /**
     * @param mask if mask can be refreshed during the game, will assume each ult
     */
    public EarthlyEscapade(AbstractCharacter<?> owner, boolean mask) {
        super(1164, 529, 463, owner);
        this.mask = mask;
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_DAMAGE, 32, "Earthly Escapade Crit Damage Boost"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        getBattle().registerForPlayers(p -> p.addPower(new Mask(mask)));
    }

    public static class Mask extends AbstractPower {
        public Mask(boolean perm) {
            this.setName(this.getClass().getSimpleName());
            this.setStat(PowerStat.CRIT_DAMAGE, 28);
            this.setStat(PowerStat.CRIT_CHANCE, 10);
            if (perm) {
                this.lastsForever = true;
            } else {
                this.turnDuration = 3;
            }
        }
    }
}
