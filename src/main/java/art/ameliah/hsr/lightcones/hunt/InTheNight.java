package art.ameliah.hsr.lightcones.hunt;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.List;

public class InTheNight extends AbstractLightcone {

    public InTheNight(AbstractCharacter<?> owner) {
        super(1058, 582, 463, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_CHANCE, 18, "In The Night Crit Chance Boost"));
        this.owner.addPower(new InTheNightPower());
    }

    public static class InTheNightPower extends PermPower {
        public InTheNightPower() {
            this.setName(this.getClass().getSimpleName());
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (!damageTypes.contains(DamageType.SKILL) && !damageTypes.contains(DamageType.BASIC)) return 0;
            if (getOwner() != character) return 0;
            if (character.getFinalSpeed() < 100) return 0;
            int stacks = Math.min(6, (int) ((character.getFinalSpeed() - 100) / 10));
            return stacks * 6;
        }

        @Override
        public float getConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (!damageTypes.contains(DamageType.ULTIMATE)) return 0;
            if (getOwner() != character) return 0;
            if (character.getFinalSpeed() < 100) return 0;
            int stacks = Math.min(6, (int) ((character.getFinalSpeed() - 100) / 10));
            return stacks * 12;
        }
    }
}
