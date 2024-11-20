package art.ameliah.hsr.lightcones.abundance;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import java.util.ArrayList;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class EchoesOftheCoffin extends AbstractLightcone {

    public EchoesOftheCoffin(AbstractCharacter<?> owner) {
        super(1164, 582, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.ATK_PERCENT, 24, "Echoes of the Coffin ATK Boost"));
    }

    @Override
    public void onUseUltimate() {
        getBattle().getPlayers().forEach(c -> {
            getBattle().IncreaseSpeed(c, TempPower.create(PowerStat.FLAT_SPEED, 12, 1, "Echoes of the Coffin Speed Boost"));
        });
    }

    @Override
    public void onAttack(AbstractCharacter<?> character, ArrayList<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
        int stacks = Math.min(3, enemiesHit.size());
        this.owner.increaseEnergy(3 * stacks, AbstractCharacter.LIGHTCONE_ENERGY_GAIN);
    }
}
