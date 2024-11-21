package art.ameliah.hsr.lightcones.preservation;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

import java.util.ArrayList;

// TODO: The wave boost isn't implemented yet, doesn't seem like Battle has a concept of waves
public class SheAlreadyShutHerEyes extends AbstractLightcone {

    public SheAlreadyShutHerEyes(AbstractCharacter<?> owner) {
        super(1270, 423, 529, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.HP_PERCENT, 24, "She Already Shut Her Eyes HP Boost"));
        this.owner.addPower(PermPower.create(PowerStat.ENERGY_REGEN, 12, "She Already Shut Her Eyes Energy Regen Boost"));
    }

    @Override
    public void onAttacked(AbstractCharacter<?> c, AbstractEnemy enemy, ArrayList<DamageType> types, int energyFromAttacked, float totalDmg) {
        // TODO: Check if owner has shield, and if it has more than the attack
        // if (...) return;

        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.addPower(TempPower.create(PowerStat.DAMAGE_BONUS, 9, 2, "She Already Shut Her Eyes Damage Boost"));
        }
    }

}
