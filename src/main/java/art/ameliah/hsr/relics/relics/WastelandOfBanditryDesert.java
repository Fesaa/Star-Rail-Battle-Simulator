package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.List;

/**
 * 4PC CD bonus is currently not working, need to implement a more complete weakness break for it
 */
public class WastelandOfBanditryDesert extends AbstractRelicSetBonus {
    public WastelandOfBanditryDesert(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public WastelandOfBanditryDesert(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.IMAGINARY_DMG_BOOST, 10, "Genius of Brilliant Stars Quantum bonus"));
    }

    public static class WastelandOfBanditryDesert4PC extends PermPower {
        public WastelandOfBanditryDesert4PC(AbstractCharacter<?> owner) {
            super("Wasteland of Banditry Desert 4PC bonus");
        }

        @Override
        public float getConditionalCritRate(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (enemy.powerList.stream().noneMatch(p -> p.type == PowerType.DEBUFF)) {
                return 0;
            }

            return 10;
        }

        @Override
        public float getConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            //if (enemy.powerList.stream().noneMatch(p -> p.name.equals(WeaknessBreak.IMPRISONED))) {
            //    return 0;
            //}
            // return 20;

            return 0;
        }
    }
}
