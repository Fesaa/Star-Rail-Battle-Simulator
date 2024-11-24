package art.ameliah.hsr.lightcones.abundance;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.lightcones.AbstractLightcone;

public class Multiplication extends AbstractLightcone {

    public Multiplication(AbstractCharacter<?> owner) {
        super(953, 318, 198, owner);
    }

    public void beforeAttack(Attack attack) {
        if (attack.getTypes().contains(DamageType.BASIC)) {
            getBattle().AdvanceEntity(attack.getSource(), 20);
        }
    }

}
