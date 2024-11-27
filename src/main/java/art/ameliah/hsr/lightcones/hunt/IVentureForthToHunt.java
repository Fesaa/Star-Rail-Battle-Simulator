package art.ameliah.hsr.lightcones.hunt;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.List;

public class IVentureForthToHunt extends AbstractLightcone {

    public IVentureForthToHunt(AbstractCharacter<?> owner) {
        super(953, 635, 463, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_CHANCE, 15, "I Venture Forth to Hunt Crit Chance Boost"));
        this.owner.addPower(new VentureForthPower());
    }

    public String toString() {
        return "I Venture Forth to Hunt";
    }

    public static class VentureForthPower extends AbstractPower {
        public VentureForthPower() {
            this.setName(this.getClass().getSimpleName());
            this.lastsForever = true;
        }

        @Override
        public void onEndTurn() {
            this.stacks = Math.max(0, this.stacks - 1);
        }

        @Override
        public float getConditionDefenseIgnore(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (damageTypes.contains(DamageType.ULTIMATE) && this.stacks > 0) {
                return 27 * this.stacks;
            }
            return 0;
        }

        @Override
        public void beforeAttack(AttackLogic attack) {
            if (attack.getTypes().contains(DamageType.FOLLOW_UP)) {
                this.stacks = Math.min(2, this.stacks + 1);
            }
        }
    }
}
