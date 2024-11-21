package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import java.util.ArrayList;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class Fermata extends AbstractLightcone {

    public Fermata(AbstractCharacter<?> owner) {
        super(953, 476, 331, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(new FermataBoost());
    }

    public static class FermataBoost extends PermPower {
        public FermataBoost() {
            this.name = this.getClass().getSimpleName();
            this.setStat(PowerStat.BREAK_EFFECT, 32);
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            // TODO: Check for Wind & Shock

            return 32;
        }
    }
}
