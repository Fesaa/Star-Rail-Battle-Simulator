package art.ameliah.hsr.lightcones.destruction;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

import java.util.ArrayList;

public class SomethingIrreplaceable extends AbstractLightcone {

    public SomethingIrreplaceable(AbstractCharacter<?> owner) {
        super(1164, 582, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.ATK_PERCENT, 24, "Something Irreplaceable ATK Boost"));
    }

    @Override
    public void onAttacked(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> types, int energyFromAttacked, float totalDmg) {
        // TODO: Restore HP
        TempPower power = TempPower.create(PowerStat.DAMAGE_BONUS, 24, 1, "Something Irreplaceable Damage Bonus");
        this.owner.addPower(power);
    }
}
