package art.ameliah.hsr.lightcones.destruction;

import art.ameliah.hsr.battleLogic.combat.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class SomethingIrreplaceable extends AbstractLightcone {

    public SomethingIrreplaceable(AbstractCharacter<?> owner) {
        super(1164, 582, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.ATK_PERCENT, 24, "Something Irreplaceable ATK Boost"));
    }

    @Override
    public void afterAttacked(AttackLogic attack) {
        // TODO: Restore HP
        TempPower power = TempPower.create(PowerStat.DAMAGE_BONUS, 24, 1, "Something Irreplaceable Damage Bonus");
        this.owner.addPower(power);
    }
}
