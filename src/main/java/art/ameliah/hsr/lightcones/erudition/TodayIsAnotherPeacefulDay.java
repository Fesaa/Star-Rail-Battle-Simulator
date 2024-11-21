package art.ameliah.hsr.lightcones.erudition;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class TodayIsAnotherPeacefulDay extends AbstractLightcone {

    public TodayIsAnotherPeacefulDay(AbstractCharacter<?> owner) {
        super(847, 529, 331, owner);
    }

    @Override
    public void onCombatStart() {
        float boost = (float) 0.4 * Math.min(160, this.owner.maxEnergy);
        this.owner.addPower(PermPower.create(PowerStat.DAMAGE_BONUS, boost, "Today is Another Peaceful Day Damage Boost"));
    }
}
