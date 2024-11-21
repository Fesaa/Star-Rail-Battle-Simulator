package art.ameliah.hsr.lightcones.erudition;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

import java.util.ArrayList;
import java.util.Set;

public class YetHopeIsPriceless extends AbstractLightcone {

    public YetHopeIsPriceless(AbstractCharacter<?> owner) {
        super(953, 582, 529, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_CHANCE, 16, "Yet Hope Is Priceless Crit Chance Boost"));
        this.owner.addPower(new YetHopeIsPricelessPower());
    }

    @Override
    public void onAttack(AbstractCharacter<?> character, Set<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
        if (!types.contains(DamageType.BASIC)) return;

        this.owner.addPower(TempPower.create(PowerStat.DEFENSE_IGNORE, 20, 2, "Yet Hope Is Priceless Defense Ignore Debuff"));
    }

    public static class YetHopeIsPricelessPower extends AbstractPower {
        public YetHopeIsPricelessPower() {
            this.name = this.getClass().getSimpleName();
            this.lastsForever = true;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            if (!damageTypes.contains(DamageType.FOLLOW_UP)) return 0;
            if (owner != character) return 0;
            if (character.getTotalCritDamage() < 120) return 0;

            int stacks = Math.min(4, (int) (character.getTotalCritDamage() - 120)/20);
            return 12 * stacks;
        }
    }
}
