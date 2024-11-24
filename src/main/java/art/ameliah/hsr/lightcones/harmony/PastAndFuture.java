package art.ameliah.hsr.lightcones.harmony;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

/**
 * Assumes onSpecificTrigger is called on skill, with the character as next to act
 */
public class PastAndFuture extends AbstractLightcone {

    public PastAndFuture(AbstractCharacter<?> owner) {
        super(953, 423, 397, owner);
    }

    public void onSpecificTrigger(AbstractCharacter<?> character, AbstractEnemy enemy) {
        if (character != null) {
            character.addPower(TempPower.create(PowerStat.DAMAGE_BONUS, 32, 1, "Past and Future Damage Boost"));
        }
    }

    public String toString() {
        return "Past and Future";
    }
}
