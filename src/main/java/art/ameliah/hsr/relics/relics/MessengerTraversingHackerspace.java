package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

/**
 * Currently no check if ultimate is used on ally, don't add the 4PC if the character can't do this
 */
public class MessengerTraversingHackerspace extends AbstractRelicSetBonus {

    public MessengerTraversingHackerspace(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public MessengerTraversingHackerspace(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.SPEED_PERCENT, 6, "Messenger Traversing Hackerspace 2PC"));
    }

    @Override
    public void onUseUltimate() {
        if (!this.isFullSet) return;

        getBattle().getPlayers().forEach(c -> {
            TempPower ultPower = TempPower.create(PowerStat.SPEED_PERCENT, 12, 1, "Messenger Traversing Hackerspace 4PC ");
            getBattle().IncreaseSpeed(c, ultPower);
        });
    }
}
