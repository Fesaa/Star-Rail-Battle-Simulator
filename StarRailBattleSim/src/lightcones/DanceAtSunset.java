package lightcones;

import characters.AbstractCharacter;
import enemies.AbstractEnemy;
import powers.AbstractPower;
import powers.PermPower;

import java.util.ArrayList;

public class DanceAtSunset extends AbstractLightcone {

    public DanceAtSunset(AbstractCharacter owner) {
        super(1058, 582, 463, owner);
    }

    @Override
    public void onEquip() {
        PermPower statBonus = new PermPower();
        statBonus.name = "Dance At Sunset Stat Bonus";
        statBonus.bonusCritDamage = 36;
        statBonus.bonusTauntValue = 500;
        owner.addPower(statBonus);
    }

    public void onUseUltimate() {
        owner.addPower(new DanceAtSunsetDamagePower());
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    private static class DanceAtSunsetDamagePower extends AbstractPower {
        public DanceAtSunsetDamagePower() {
            this.name = this.getClass().getSimpleName();
            this.turnDuration = 2;
            this.maxStacks = 2;
        }
        @Override
        public float getConditionalDamageBonus(AbstractCharacter character, AbstractEnemy enemy, ArrayList<AbstractCharacter.DamageType> damageTypes) {
            for (AbstractCharacter.DamageType type : damageTypes) {
                if (type == AbstractCharacter.DamageType.FOLLOW_UP) {
                    return 36 * stacks;
                }
            }
            return 0;
        }
    }
}