package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;

import java.util.ArrayList;

public class GoodNightAndSleepWell extends AbstractLightcone {

    public GoodNightAndSleepWell(AbstractCharacter<?> owner) {
        super(953, 476, 331, owner);
    }

    public static class GoodNightAndSleepWellPower extends PermPower {
        public GoodNightAndSleepWellPower() {
            this.name = this.getClass().getSimpleName();
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            int mul = (int) enemy.powerList.stream().filter(p -> p.type.equals(PowerType.DEBUFF)).count();
            return 24 * Math.min(3, mul);
        }
    }


}
