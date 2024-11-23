package art.ameliah.hsr.lightcones.erudition;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.List;

public class BeforeDawn extends AbstractLightcone {

    public BeforeDawn(AbstractCharacter<?> owner) {
        super(1058, 582, 463, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_DAMAGE, 36, "Before Dawn Crit Damage Boost"));
        this.owner.addPower(new BeforeDawnPower());
    }

    public static class BeforeDawnPower extends AbstractPower {
        private boolean hasSomnusCorpus = false;

        public BeforeDawnPower() {
            this.setName(this.getClass().getSimpleName());
            this.lastsForever = true;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (damageTypes.contains(DamageType.FOLLOW_UP) && hasSomnusCorpus) {
                this.hasSomnusCorpus = false;
                return 48;
            }

            if (damageTypes.contains(DamageType.SKILL) || damageTypes.contains(DamageType.ULTIMATE)) {
                this.hasSomnusCorpus = true;
                return 18;
            }

            return 0;
        }
    }
}
