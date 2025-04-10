package art.ameliah.hsr.relics.ornament;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreAllyAttack;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class ForgeOfTheKalpagniLatern extends AbstractRelicSetBonus {
    public ForgeOfTheKalpagniLatern(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public ForgeOfTheKalpagniLatern(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.SPEED_PERCENT, 6, "Forge Of The Kalpagni Latern SPD Boost"));
    }


    @Subscribe
    public void beforeAttack(PreAllyAttack event) {
        if (event.getAttack().getTargets().stream().anyMatch(e -> e.hasWeakness(ElementType.FIRE))) {
            this.owner.addPower(TempPower.create(PowerStat.BREAK_EFFECT, 40, 1, "Forge Of The Kalpagni Latern Break Bonus"));
        }
    }
}
