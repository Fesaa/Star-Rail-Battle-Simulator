package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.List;

public class IronCavalryAgainstTheScourge extends AbstractRelicSetBonus {
    public IronCavalryAgainstTheScourge(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public IronCavalryAgainstTheScourge(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.BREAK_EFFECT, 16, "Iron Cavalry Against The Scourge 2PC break bonus"));

        if (this.isFullSet) {
            this.owner.addPower(new IronCavalryAgainstTheScourge4PC());
        }
    }

    public static class IronCavalryAgainstTheScourge4PC extends PermPower {
        public IronCavalryAgainstTheScourge4PC() {
            super("Iron Cavalry Against The Scourge 4PC");
        }

        @Override
        public float getConditionDefenseIgnore(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (damageTypes.contains(DamageType.SUPER_BREAK)) {
                return 25;
            }

            if (damageTypes.contains(DamageType.BREAK)) {
                return 10;
            }

            return 0;
        }
    }
}
