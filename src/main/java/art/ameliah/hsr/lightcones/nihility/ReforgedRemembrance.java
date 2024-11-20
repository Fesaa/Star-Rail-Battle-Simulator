package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.ArrayList;

/**
 * No ramp-up for Prophet
 */
public class ReforgedRemembrance extends AbstractLightcone {

    public ReforgedRemembrance(AbstractCharacter<?> owner) {
        super(1058, 582, 463, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.EFFECT_HIT, 40, "Reforged Remembrance Effect Hit Boost"));
        Prophet prophet = new Prophet();
        prophet.stacks = 4;
        this.owner.addPower(prophet);
    }

    public static class Prophet extends PermPower {
        public Prophet() {
            this.name = this.getClass().getSimpleName();
            this.setStat(PowerStat.ATK_PERCENT, 5);
        }

        @Override
        public float getConditionDefenseIgnore(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            if (!damageTypes.contains(DamageType.DOT)) return 0;

            return 7.2f * this.stacks;
        }
    }
}
