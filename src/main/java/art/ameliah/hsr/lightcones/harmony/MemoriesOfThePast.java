package art.ameliah.hsr.lightcones.harmony;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import java.util.ArrayList;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class MemoriesOfThePast extends AbstractLightcone {

    private boolean canRegen = true;

    public MemoriesOfThePast(AbstractCharacter<?> owner) {
        super(953, 423, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.BREAK_EFFECT, 56, "Memories of the Past Break Effect Boost"));
    }

    @Override
    public void onTurnStart() {
        this.canRegen = true;
    }

    @Override
    public void onAttack(AbstractCharacter<?> character, ArrayList<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
        if (!this.canRegen) return;

        this.owner.increaseEnergy(8, AbstractCharacter.LIGHTCONE_ENERGY_GAIN);
        this.canRegen = false;
    }
}
