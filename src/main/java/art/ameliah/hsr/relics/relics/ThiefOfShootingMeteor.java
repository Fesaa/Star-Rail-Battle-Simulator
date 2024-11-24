package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

/**
 * Energy generation is not implemented yet, so this relic is not complete.
 */
public class ThiefOfShootingMeteor extends AbstractRelicSetBonus {
    public ThiefOfShootingMeteor(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public ThiefOfShootingMeteor(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        PermPower breakBoost = PermPower.create(PowerStat.BREAK_EFFECT, 16, "Thief of Shooting Meteor Break Effect Boost");
        if (this.isFullSet) {
            breakBoost.increaseStat(PowerStat.BREAK_EFFECT, 16);
        }
        this.owner.addPower(breakBoost);
    }

    @Override
    public void afterAttack(Attack attack) {
        // TODO: Generate 3 energy after weakness breaking, don't see a way to check if an enemy was broken this turn
    }
}
