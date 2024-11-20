package lightcones.harmony;

import characters.AbstractCharacter;
import characters.DamageType;
import enemies.AbstractEnemy;
import lightcones.AbstractLightcone;
import powers.AbstractPower;
import powers.PermPower;

import java.util.ArrayList;

public class DreamvilleAdventure extends AbstractLightcone {

    private DamageType currBenefit;
    AbstractPower childishness = new Childishness();

    public DreamvilleAdventure(AbstractCharacter<?> owner) {
        super(953, 423, 397, owner);
    }

    @Override
    public void onUseSkill() {
        currBenefit = DamageType.SKILL;
    }

    @Override
    public void onUseBasic() {
        currBenefit = DamageType.BASIC;
    }

    @Override
    public void onUseUltimate() {
        currBenefit = DamageType.ULTIMATE;
    }

    @Override
    public void onCombatStart() {
        getBattle().getPlayers().forEach(c -> c.addPower(childishness));
    }

    public class Childishness extends PermPower {
        public Childishness() {
            this.name = this.getClass().getSimpleName();
        }
        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            if (currBenefit == null) {
                return 0;
            }
            if (damageTypes.contains(currBenefit)) {
                return 20;
            }
            return 0;
        }
    }
}
