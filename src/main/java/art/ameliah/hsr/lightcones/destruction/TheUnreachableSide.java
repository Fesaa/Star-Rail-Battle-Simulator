package art.ameliah.hsr.lightcones.destruction;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostAllyAttack;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.List;

public class TheUnreachableSide extends AbstractLightcone {
    public TheUnreachableSide(AbstractCharacter<?> owner) {
        super(1270, 582, 331, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_CHANCE, 18, "The Unreachable Side CR Boost"));
        this.owner.addPower(PermPower.create(PowerStat.HP_PERCENT, 18, "The Unreachable Side HP Boost"));
    }

    // TODO: On consuming health
    public static class TheUnreachableSideDMGBoost extends AbstractPower {

        private boolean active = true;

        public TheUnreachableSideDMGBoost() {
            this.setName(this.getClass().getSimpleName());
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (this.active) {
                this.active = false;
                return 24;
            }

            return 0;
        }

        @Subscribe
        public void afterAttacked(PostAllyAttack e) {
            this.active = true;
        }
    }
}
