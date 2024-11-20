package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import java.util.ArrayList;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class EyesOfThePrey extends AbstractLightcone {

    public EyesOfThePrey(AbstractCharacter<?> owner) {
        super(953, 476, 331, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(new EyesOfThePreyPower());
    }

    public static class EyesOfThePreyPower extends PermPower {
        public EyesOfThePreyPower() {
            this.name = this.getClass().getSimpleName();
            this.setStat(PowerStat.EFFECT_HIT, 40);
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            if (!damageTypes.contains(DamageType.DOT)) return 0;

            return 48;
        }
    }
}
