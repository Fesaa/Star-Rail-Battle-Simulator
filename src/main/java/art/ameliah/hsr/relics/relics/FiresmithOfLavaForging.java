package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.List;

public class FiresmithOfLavaForging extends AbstractRelicSetBonus {
    public FiresmithOfLavaForging(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public FiresmithOfLavaForging(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.FIRE_DMG_BOOST, 10, "Firesmith of Lave Forging Fire Bonus"));

        if (this.isFullSet) {
            this.owner.addPower(new FiresmithOfLavaForging4PC());
        }
    }

    public static class FiresmithOfLavaForging4PC extends PermPower {
        public FiresmithOfLavaForging4PC() {
            super("Firesmith of Lave Forging Fire Bonus 4PC");
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (damageTypes.contains(DamageType.SKILL)) {
                return 12;
            }

            return 0;
        }

        // TODO: onAfterUseUltimate
        @Override
        public void afterAttack(AttackLogic attack) {
            if (!attack.getTypes().contains(DamageType.ULTIMATE)) return;

            this.getOwner().addPower(TempPower.create(PowerStat.FIRE_DMG_BOOST, 12, 1, "Firesmith of Lave Forging Fire Bonus 4PC Ultimate Bonus"));
        }
    }
}
