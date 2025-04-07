package art.ameliah.hsr.lightcones.hunt;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

/**
 * Assumes 100% uptime
 */
public class RiverFlowsInSpring extends AbstractLightcone {

    public RiverFlowsInSpring(AbstractCharacter<?> owner) {
        super(847, 476, 397, owner);
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        this.owner.addPower(PermPower.create(PowerStat.SPEED_PERCENT, 12, "River Flows In Spring Speed Boost"));
        this.owner.addPower(PermPower.create(PowerStat.DAMAGE_BONUS, 24, "River Flows In Spring Damage Boost"));
    }
}
