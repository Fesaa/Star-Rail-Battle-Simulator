package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostSummon;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class GiantTreeOfRaptBrooding extends AbstractRelicSetBonus {

    private final PermPower healingPower = PermPower.create(PowerStat.OUTGOING_HEALING, (c) -> {

        if (c.getFinalSpeed() >= 180) {
            return 20f;
        }

        if (c.getFinalSpeed() > 135) {
            return 12f;
        }

        return 0f;
    }, "Giant Tree of Rapt Brooding Healing boost");

    public GiantTreeOfRaptBrooding(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.SPEED_PERCENT, 6, "Giant Tree of Rapt Brooding SPD boost"));
    }

    @Subscribe
    public void onMemoSpawn(PostSummon e) {
        e.getMemosprite().addPower(healingPower);
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent e) {
        this.owner.addPower(healingPower);
    }
}
