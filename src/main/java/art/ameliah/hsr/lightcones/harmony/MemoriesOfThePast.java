package art.ameliah.hsr.lightcones.harmony;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostAllyAttack;
import art.ameliah.hsr.events.combat.TurnStartEvent;
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

    @Subscribe
    public void onTurnStart(TurnStartEvent event) {
        this.canRegen = true;
    }

    @Subscribe
    public void afterAttack(PostAllyAttack event) {
        if (!this.canRegen) return;

        this.owner.increaseEnergy(8, AbstractCharacter.LIGHTCONE_ENERGY_GAIN);
        this.canRegen = false;
    }
}
