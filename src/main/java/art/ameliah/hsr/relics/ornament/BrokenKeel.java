package art.ameliah.hsr.relics.ornament;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import java.util.ArrayList;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class BrokenKeel extends AbstractRelicSetBonus {
    public BrokenKeel(AbstractCharacter<?> owner) {
        super(owner);
    }

    public void onEquip() {
        owner.addPower(PermPower.create(PowerStat.EFFECT_RES, 10, "Broken Keel Effect Resistance Bonus"));
    }

    public void onCombatStart() {
        getBattle().getPlayers().forEach(character -> character.addPower(new BrokenKeelStackPower()));
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    private class BrokenKeelStackPower extends PermPower {
        public BrokenKeelStackPower() {
            // If more than one character has this relic, the relic should not stack. As the CRIT DMG is conditional per wearer
            this.name = this.getClass().getSimpleName() + "-" + BrokenKeel.this.owner.name;
        }

        @Override
        public float getConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            if (BrokenKeel.this.owner.getTotalEffectRes() < 30) {
                return 0;
            }

            return 10;
        }
    }

}