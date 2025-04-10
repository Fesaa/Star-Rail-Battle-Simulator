package art.ameliah.hsr.relics.ornament;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class PanCosmicCommercialEnterprise extends AbstractRelicSetBonus {
    public PanCosmicCommercialEnterprise(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public PanCosmicCommercialEnterprise(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.EFFECT_HIT, 10, "Pan Cosmic Commercial Enterprise effect hit bonus"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        float bonus = Math.min(25, this.owner.getTotalEHR() / 25);
        this.owner.addPower(PermPower.create(PowerStat.ATK_PERCENT, bonus, "Pan Cosmic Commercial Enterprise ATK bonus"));
    }
}
