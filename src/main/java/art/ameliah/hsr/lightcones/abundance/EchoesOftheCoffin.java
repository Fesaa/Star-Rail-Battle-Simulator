package art.ameliah.hsr.lightcones.abundance;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.characters.AbstractCharacter;
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
        getBattle().getPlayers().forEach(c -> getBattle().IncreaseSpeed(c, TempPower.create(PowerStat.FLAT_SPEED, 12, 1, "Echoes of the Coffin Speed Boost")));
    }

    @Override
    public void onAttack(Attack attack) {
        int stacks = Math.min(3, attack.getTargets().size());
        this.owner.increaseEnergy(3 * stacks, AbstractCharacter.LIGHTCONE_ENERGY_GAIN);
    }
}
