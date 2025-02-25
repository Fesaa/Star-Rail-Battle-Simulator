package art.ameliah.hsr.relics.ornament;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.EnemyLeavesCombat;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.List;

/**
 * Enemies don't actually die, so the CD boost can't be implemented.
 * You may pass a % uptime.
 */
public class SigoniaTheUnclaimedDesolation extends AbstractRelicSetBonus {

    public SigoniaTheUnclaimedDesolation(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public SigoniaTheUnclaimedDesolation(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_CHANCE, 4, "Sigonia The Unclaimed Desolation CR Boost"));
    }

    public static class SigoniaTheUnclaimedDesolationCDPower extends PermPower {
        public static final String NAME = "Sigonia The Unclaimed Desolation CR Boost";

        public SigoniaTheUnclaimedDesolationCDPower() {
            super(NAME);
        }

        @Subscribe
        public void onEnemyRemove(EnemyLeavesCombat event) {
            this.stacks = Math.min(this.stacks+1, 10);
        }

        @Override
        public float getConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            return this.stacks*10;
        }
    }
}
