package art.ameliah.hsr.lightcones.abundance;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreAllyAttack;
import art.ameliah.hsr.events.character.PreUltimate;
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

    @Subscribe
    public void onUseUltimate(PreUltimate e) {
        getBattle().getPlayers().forEach(c -> getBattle().IncreaseSpeed(c, TempPower.create(PowerStat.FLAT_SPEED, 12, 1, "Echoes of the Coffin Speed Boost")));
    }

    @Subscribe
    public void beforeAttack(PreAllyAttack e) {
        int stacks = Math.min(3, e.getAttack().getTargets().size());
        this.owner.increaseEnergy(3 * stacks, AbstractCharacter.LIGHTCONE_ENERGY_GAIN);
    }
}
