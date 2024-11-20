package art.ameliah.hsr.lightcones.abundance;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class SharedFeeling extends AbstractLightcone {

    public SharedFeeling( AbstractCharacter<?> owner) {
        super(953, 423, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.HEALING, 20, "Shared Feeling Healing Boost"));
    }

    @Override
    public void onUseSkill() {
        getBattle().getPlayers().forEach(c -> c.increaseEnergy(4, AbstractCharacter.LIGHTCONE_ENERGY_GAIN));
    }
}