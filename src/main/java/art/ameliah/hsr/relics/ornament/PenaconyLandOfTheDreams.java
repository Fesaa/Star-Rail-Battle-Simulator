package art.ameliah.hsr.relics.ornament;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class PenaconyLandOfTheDreams extends AbstractRelicSetBonus {
    public PenaconyLandOfTheDreams(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public PenaconyLandOfTheDreams(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.ENERGY_REGEN, 5, "Penacony Land of the Dreams energy regen"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        getBattle().getPlayers().stream()
                .filter(c -> c.elementType == this.owner.elementType)
                .filter(c -> c != this.owner)
                .forEach(c -> c.addPower(PermPower.create(PowerStat.DAMAGE_BONUS, 10, "Penacony Land of the Dreams damage bonus")));
    }
}
