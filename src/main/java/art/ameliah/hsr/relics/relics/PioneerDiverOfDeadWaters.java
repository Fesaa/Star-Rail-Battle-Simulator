package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.List;

/**
 * Assumes the character always inflicts a debuff every turn.
 */
public class PioneerDiverOfDeadWaters extends AbstractRelicSetBonus {
    public PioneerDiverOfDeadWaters(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public PioneerDiverOfDeadWaters(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(new PioneerDiverOfDeadWaters2PC());
        if (this.isFullSet) {
            this.owner.addPower(new PioneerDiverOfDeadWaters4PC());
        }
    }

    public static class PioneerDiverOfDeadWaters2PC extends PermPower {
        public PioneerDiverOfDeadWaters2PC() {
            this.setName(this.getClass().getSimpleName());
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (enemy.powerList.stream().anyMatch(p -> p.type == PowerType.DEBUFF)) {
                return 12;
            }
            return 0;
        }
    }

    public static class PioneerDiverOfDeadWaters4PC extends PermPower {

        public PioneerDiverOfDeadWaters4PC() {
            this.setName(this.getClass().getSimpleName());
            this.setStat(PowerStat.CRIT_CHANCE, 4);
        }

        @Override
        public float getConditionalCritRate(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            return 4;
        }

        @Override
        public float getConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            int debuffs = Math.min(3, (int) enemy.powerList.stream().filter(p -> p.type == PowerType.DEBUFF).count());
            if (debuffs < 2) {
                return 0;
            }
            return 8 * debuffs;
        }
    }

}
