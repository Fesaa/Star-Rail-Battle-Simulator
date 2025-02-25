package art.ameliah.hsr.lightcones.erudition;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreAllyAttack;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class TheDayTheCosmosFell extends AbstractLightcone {

    public TheDayTheCosmosFell(AbstractCharacter<?> owner) {
        super(953, 476, 331, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.ATK_PERCENT, 24, "The Day The Cosmos Fell Attack Boost"));
    }

    @Subscribe
    public void beforeAttack(PreAllyAttack event) {
        if (event.getAttack().getTargets().stream()
                .filter(e -> e.hasWeakness(this.owner.elementType))
                .count() < 2) return;

        this.owner.addPower(TempPower.create(PowerStat.CRIT_DAMAGE, 40, 2, "The Day The Cosmos Fell Crit Damage Boost"));
    }
}
