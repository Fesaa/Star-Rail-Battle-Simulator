package art.ameliah.hsr.lightcones.harmony;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class PastSelfInMirror extends AbstractLightcone {

    public PastSelfInMirror(AbstractCharacter<?> owner) {
        super(1058, 529, 529, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.BREAK_EFFECT, 60, "Past Self in Mirror Break Effect Boost"));
    }

    @Override
    public void onCombatStart() {
        getBattle().getPlayers().stream()
                .filter(c -> c.usesEnergy)
                .forEach(c -> c.increaseEnergy(10, false, AbstractCharacter.LIGHTCONE_ENERGY_GAIN));
    }

    @Override
    public void onUseUltimate() {
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.addPower(TempPower.create(PowerStat.DAMAGE_BONUS, 24, 3, "Past Self in Mirror Damage Boost"));
        }
        if (this.owner.getTotalBreakEffect() > 150) {
            getBattle().generateSkillPoint(this.owner, 1);
        }
    }
}