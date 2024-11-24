package art.ameliah.hsr.lightcones.erudition;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.List;

public class AnInstantBeforeAGaze extends AbstractLightcone {

    public AnInstantBeforeAGaze(AbstractCharacter<?> owner) {
        super(1058, 582, 463, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_DAMAGE, 36, "An Instant Before A Gaze Crit Damage Boost"));
        this.owner.addPower(new AnInstantBeforeAGazePower());
    }

    public static class AnInstantBeforeAGazePower extends AbstractPower {
        public AnInstantBeforeAGazePower() {
            this.setName(this.getClass().getSimpleName());
            this.lastsForever = true;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (!damageTypes.contains(DamageType.ULTIMATE)) return 0;
            if (character != getOwner()) return 0;

            return (float) (Math.min(character.maxEnergy, 180) * 0.36);

        }
    }
}
