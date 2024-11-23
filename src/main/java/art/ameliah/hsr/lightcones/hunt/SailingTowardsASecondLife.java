package art.ameliah.hsr.lightcones.hunt;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.List;

public class SailingTowardsASecondLife extends AbstractLightcone {

    public SailingTowardsASecondLife(AbstractCharacter<?> owner) {
        super(1058, 582, 463, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.BREAK_EFFECT, 60, "Sailing Towards A Second Life Break Effect Boost"));
        this.owner.addPower(new SailingTowardsASecondLifePower());
    }

    @Override
    public void onCombatStart() {
        if (this.owner.getTotalBreakEffect() > 150) {
            this.owner.addPower(PermPower.create(PowerStat.SPEED_PERCENT, 12, "Sailing Towards A Second Life Speed Boost"));
        }
    }

    public static class SailingTowardsASecondLifePower extends PermPower {
        public SailingTowardsASecondLifePower() {
            this.setName(this.getClass().getSimpleName());
        }

        @Override
        public float getConditionDefenseIgnore(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (!damageTypes.contains(DamageType.BREAK)) return 0;

            return 20;
        }
    }
}
