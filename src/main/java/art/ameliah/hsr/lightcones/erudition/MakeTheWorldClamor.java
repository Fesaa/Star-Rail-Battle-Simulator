package art.ameliah.hsr.lightcones.erudition;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import java.util.ArrayList;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;

public class MakeTheWorldClamor extends AbstractLightcone {

    public MakeTheWorldClamor(AbstractCharacter<?> owner) {
        super(847, 476, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(new MakeTheWorldClamorPower());
    }

    @Override
    public void onCombatStart() {
        if (this.owner.usesEnergy)
            this.owner.increaseEnergy(32, AbstractCharacter.LIGHTCONE_ENERGY_GAIN);
    }

    public static class MakeTheWorldClamorPower extends PermPower {
        public MakeTheWorldClamorPower() {
            this.name = this.getClass().getSimpleName();
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            if (!damageTypes.contains(DamageType.ULTIMATE)) return 0;

            return 64;
        }
    }
}
