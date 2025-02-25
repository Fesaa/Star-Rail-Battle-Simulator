package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.events.enemy.WeaknessBreakEvent;
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

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        getBattle().registerForEnemy(e -> e.addPower(new ThiefOfShootingMeteorBreakListener()));
    }

    public class ThiefOfShootingMeteorBreakListener extends PermPower {

        @Subscribe
        public void onWeaknessBreak(WeaknessBreakEvent event) {
            if (ThiefOfShootingMeteor.this.owner.equals(event.getSource())) {
                ThiefOfShootingMeteor.this.owner.increaseEnergy(3, "Thief Of Shooting Meteor");
            }
        }

    }
}
