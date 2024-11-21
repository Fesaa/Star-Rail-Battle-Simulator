package art.ameliah.hsr.lightcones.preservation;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import java.util.ArrayList;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class DestinysThreadsForewoven extends AbstractLightcone {

    public DestinysThreadsForewoven(AbstractCharacter<?> owner) {
        super(953, 370, 463, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.EFFECT_RES, 20, "Destiny's Threads Forewoven Effect Resistance Boost"));
        this.owner.addPower(new DestinysThreadsForewovenPower());
    }

    public static class DestinysThreadsForewovenPower extends AbstractPower {
        public DestinysThreadsForewovenPower() {
            this.name = this.getClass().getSimpleName();
            this.lastsForever = true;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            if (character != owner) return 0;
            return Math.min((float) (((int) (character.getFinalDefense() / 100)) * 1.2), 48);
        }
    }
}
