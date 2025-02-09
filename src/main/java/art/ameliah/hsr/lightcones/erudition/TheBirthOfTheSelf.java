package art.ameliah.hsr.lightcones.erudition;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;

import java.util.List;

public class TheBirthOfTheSelf extends AbstractLightcone {

    public TheBirthOfTheSelf(AbstractCharacter<?> owner) {
        super(953, 476, 331, owner);
    }

    public static class TheBirthOfTheSelfPower extends PermPower {
        public TheBirthOfTheSelfPower() {
            this.setName(this.getClass().getSimpleName());
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (!damageTypes.contains(DamageType.FOLLOW_UP)) return 0;
            // if (enemy.getCurrentHp() < enemy.baseHP / 2) {
            //    return 96;
            // }

            return 48;
        }
    }
}
