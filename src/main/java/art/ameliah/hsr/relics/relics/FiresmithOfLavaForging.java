package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.ArrayList;

public class FiresmithOfLavaForging extends AbstractRelicSetBonus {
    public FiresmithOfLavaForging(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public FiresmithOfLavaForging(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        if (this.owner.elementType == ElementType.FIRE) {
            this.owner.addPower(PermPower.create(PowerStat.SAME_ELEMENT_DAMAGE_BONUS, 10, "Firesmith of Lave Forging Fire Bonus"));
        }

        if (this.isFullSet) {
            this.owner.addPower(new FiresmithOfLavaForging4PC());
        }
    }

    public static class FiresmithOfLavaForging4PC extends PermPower {
        public FiresmithOfLavaForging4PC() {
            super("Firesmith of Lave Forging Fire Bonus 4PC");
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            if (damageTypes.contains(DamageType.SKILL)) {
                return 12;
            }

            return 0;
        }

        // TODO: onAfterUseUltimate
        @Override
        public void afterAttackFinish(AbstractCharacter<?> character, ArrayList<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
            if (!types.contains(DamageType.ULTIMATE)) return;

            this.owner.addPower(TempPower.create(PowerStat.SAME_ELEMENT_DAMAGE_BONUS, 12, 1, "Firesmith of Lave Forging Fire Bonus 4PC Ultimate Bonus"));
        }
    }
}
