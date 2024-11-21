package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import java.util.ArrayList;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

/**
 * Missing CR boost
 */
public class IncessantRain extends AbstractLightcone {

    public IncessantRain(AbstractCharacter<?> owner) {
        super(1058, 582, 463, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.EFFECT_HIT, 24, "Incessant Rain Effect Hit Boost"));
    }

    @Override
    public void onAttack(AbstractCharacter<?> character, ArrayList<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
        if (!types.contains(DamageType.BASIC)
                && !types.contains(DamageType.SKILL)
                && !types.contains(DamageType.ULTIMATE)) {
            return;
        }

        if (enemiesHit.isEmpty()) {
            return;
        }

        AbstractEnemy target = enemiesHit.get(getBattle().getAetherRng().nextInt(enemiesHit.size()));
        target.addPower(new AetherCode());
    }

    public static class AetherCode extends TempPower {
        public AetherCode() {
            this.name = this.getClass().getSimpleName();
            this.turnDuration = 1;
            this.setStat(PowerStat.DAMAGE_TAKEN, 12);
        }
    }
}
