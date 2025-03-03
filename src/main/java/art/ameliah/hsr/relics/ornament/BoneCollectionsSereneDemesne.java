package art.ameliah.hsr.relics.ornament;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostSummon;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class BoneCollectionsSereneDemesne extends AbstractRelicSetBonus {

    private boolean activated = false;

    public BoneCollectionsSereneDemesne(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.HP_PERCENT, 12, "Bone Collection's Serene Demesne HP Boost"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent e) {
        this.activated = this.owner.getFinalHP() >= 5000;

        this.owner.addPower(PermPower.create(PowerStat.CRIT_DAMAGE, 28, "Bone Collection's Serene Demesne CD Boost"));
    }

    @Subscribe
    public void onMemospawn(PostSummon event) {
        event.getMemosprite().addPower(PermPower.create(PowerStat.CRIT_DAMAGE, 28, "Bone Collection's Serene Demesne CD Boost"));
    }


}
