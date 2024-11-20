package lightcones.abundance;

import characters.AbstractCharacter;
import characters.DamageType;
import enemies.AbstractEnemy;
import lightcones.AbstractLightcone;

import java.util.ArrayList;

public class Multiplication extends AbstractLightcone {

    public Multiplication(AbstractCharacter<?> owner) {
        super(953, 318, 198, owner);
    }

    public void onAttack(AbstractCharacter<?> character, ArrayList<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
        if (types.contains(DamageType.BASIC)) {
            getBattle().AdvanceEntity(character, 20);
        }
    }

}
