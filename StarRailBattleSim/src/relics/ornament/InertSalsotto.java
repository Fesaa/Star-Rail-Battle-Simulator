package relics.ornament;

import characters.AbstractCharacter;
import characters.DamageType;
import enemies.AbstractEnemy;
import powers.AbstractPower;
import powers.PermPower;
import powers.PowerStat;
import relics.AbstractRelicSetBonus;

import java.util.ArrayList;

public class InertSalsotto extends AbstractRelicSetBonus {
    public InertSalsotto(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }
    public InertSalsotto(AbstractCharacter<?> owner) {
        super(owner);
    }
    public void onEquip() {
        owner.addPower(PermPower.create(PowerStat.CRIT_CHANCE, 8, "Inert Salsotto Crit Chance Bonus"));
    }

    public void onCombatStart() {
        owner.addPower(new InertSalsottoDamagePower());
    }

    private static class InertSalsottoDamagePower extends AbstractPower {
        public InertSalsottoDamagePower() {
            this.name = this.getClass().getSimpleName();
            this.lastsForever = true;
        }
        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.FOLLOW_UP || type == DamageType.ULTIMATE) {
                    return 15;
                }
            }
            return 0;
        }
    }

}
