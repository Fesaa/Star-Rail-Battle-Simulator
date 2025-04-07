package art.ameliah.hsr.lightcones.destruction;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreUltimate;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class IndeliblePromise extends AbstractLightcone {
    public IndeliblePromise(AbstractCharacter<?> owner) {
        super(953, 476, 331, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.BREAK_EFFECT, 56, "Indelible Promise Break Boost"));
    }

    @Subscribe
    public void onUseUltimate(PreUltimate e) {
        this.owner.addPower(TempPower.create(PowerStat.CRIT_CHANCE, 30, 2, "Indelible Promise Crit Boost"));
    }
}
