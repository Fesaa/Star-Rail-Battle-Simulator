package art.ameliah.hsr.lightcones.preservation;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostAllyAttacked;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

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

    @Subscribe
    public void afterAttacked(PostAllyAttacked e) {
        // TODO: Check if owner has shield, and if it has more than the attack
        // if (...) return;

        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.addPower(TempPower.create(PowerStat.DAMAGE_BONUS, 9, 2, "She Already Shut Her Eyes Damage Boost"));
        }
    }

}
