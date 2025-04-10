package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class PasserbyOfWanderingCloud extends AbstractRelicSetBonus {
    public PasserbyOfWanderingCloud(AbstractCharacter<?> owner) {
        super(owner);
    }

    public PasserbyOfWanderingCloud(AbstractCharacter<?> owner, boolean isFullSet) {
        super(owner, isFullSet);
    }


    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.OUTGOING_HEALING, 10, "Passerby of the Wandering Cloud Healing Boost"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent e) {
        if (this.isFullSet) {
            getBattle().generateSkillPoint(owner, 1);
        }
    }

    public String toString() {
        if (isFullSet) {
            return "4 PC Passerby";
        } else {
            return "2 PC Passerby";
        }
    }

}
