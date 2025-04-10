package art.ameliah.hsr.relics.ornament;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class IzumoGenseiAndTakamaDivineRealm extends AbstractRelicSetBonus {
    public IzumoGenseiAndTakamaDivineRealm(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public IzumoGenseiAndTakamaDivineRealm(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.ATK_PERCENT, 12, "Izumo Gensei And Takama Divine Realm ATK boost"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        if (getBattle().getPlayers().stream().anyMatch(c -> c.getPath() == this.owner.getPath())) {
            this.owner.addPower(PermPower.create(PowerStat.CRIT_CHANCE, 12, "Izumo Gensei And Takama Divine Realm CRIT boost"));
        }
    }
}
