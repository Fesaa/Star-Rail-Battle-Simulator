package art.ameliah.hsr.relics.ornament;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class SpringhtlyVonwacq extends AbstractRelicSetBonus {
    public SpringhtlyVonwacq(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public SpringhtlyVonwacq(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.ENERGY_REGEN, 5, "Springhtly Vonwacq energy regen"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        if (this.owner.getFinalSpeed() >= 120) {
            getBattle().AdvanceEntity(this.owner, 40);
        }
    }
}
