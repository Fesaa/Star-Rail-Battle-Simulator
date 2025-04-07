package art.ameliah.hsr.lightcones.hunt;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

/**
 * Default uptime is 50%
 */
public class CruisingInTheStellarSea extends AbstractLightcone {

    private final float upTime;

    public CruisingInTheStellarSea(AbstractCharacter<?> owner) {
        this(owner, 0.5f);
    }

    public CruisingInTheStellarSea(AbstractCharacter<?> owner, float upTime) {
        super(953, 529, 463, owner);
        this.upTime = Math.max(0, Math.min(1, upTime));
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_CHANCE, 16, "Cruising In The Stellar Sea Crit Chance Boost"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        this.owner.addPower(PermPower.create(PowerStat.ATK_PERCENT, 40 * upTime, "Cruising In The Stellar Sea Attack Boost"));
    }
}
