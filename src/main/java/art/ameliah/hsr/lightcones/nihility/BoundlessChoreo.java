package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.ArrayList;

public class BoundlessChoreo extends AbstractLightcone {

    public BoundlessChoreo(AbstractCharacter<?> owner) {
        super(953, 476, 331, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(new BoundlessChoreoCDBoost());
    }

    public static class BoundlessChoreoCDBoost extends PermPower {
        public BoundlessChoreoCDBoost() {
            this.name = this.getClass().getSimpleName();
            this.setStat(PowerStat.CRIT_CHANCE, 16);
        }

        @Override
        public float getConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            if (enemy.powerList
                    .stream()
                    .noneMatch(p -> (
                            p.getStat(PowerStat.DEFENSE_REDUCTION) != 0)
                            || p.getStat(PowerStat.SPEED_PERCENT) < 0
                    )){
                return 0;
            }

            return 48;
        }
    }
}
