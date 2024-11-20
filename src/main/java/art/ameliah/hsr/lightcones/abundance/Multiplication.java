package art.ameliah.hsr.lightcones.abundance;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import java.util.ArrayList;
import art.ameliah.hsr.lightcones.AbstractLightcone;

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