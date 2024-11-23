package art.ameliah.hsr.lightcones.hunt;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;

import java.util.List;

public class SubscribeForMore extends AbstractLightcone {

    public SubscribeForMore(AbstractCharacter<?> owner) {
        super(953, 476, 331, owner);
    }

    public static class SubscribeForMorePower extends PermPower {
        public SubscribeForMorePower() {
            this.setName(this.getClass().getSimpleName());
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (!damageTypes.contains(DamageType.SKILL) && !damageTypes.contains(DamageType.BASIC)) {
                return 0;
            }

            if (character.maxEnergy == character.currentEnergy) {
                return 48 * 2;
            }

            return 48;
        }
    }
}
