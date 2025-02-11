package art.ameliah.hsr.lightcones.preservation;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class ConcertForTwo extends AbstractLightcone {

    public ConcertForTwo(AbstractCharacter<?> owner) {
        super(953, 370, 463, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.DEF_PERCENT, 32, "Concert For Two Defense Boost"));
    }

    @Override
    public void onCombatStart() {
        // just assume full uptime for now
        this.owner.addPower(PermPower.create(PowerStat.DAMAGE_BONUS, 32, "Concert For Two Damage Boost"));
    }
}
