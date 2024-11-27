package art.ameliah.hsr.lightcones.destruction;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class BrighterThanTheSun extends AbstractLightcone {

    public BrighterThanTheSun(AbstractCharacter<?> owner) {
        super(1058, 635, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_CHANCE, 18, "Brighter Than The Sun CR Boost"));
    }

    @Override
    public void beforeAttack(AttackLogic attack) {
        if (attack.getTypes().contains(DamageType.BASIC)) {
            this.owner.addPower(new DragonsCall());
        }
    }

    public static class DragonsCall extends TempPower {
        public DragonsCall() {
            this.setName(this.getClass().getSimpleName());
            this.turnDuration = 2;
            this.maxStacks = 2;
        }

        @Override
        public float getConditionalAtkBonus(AbstractCharacter<?> character) {
            return 18 * this.stacks;
        }

        @Override
        public float getConditionalERR(AbstractCharacter<?> character) {
            return 6 * this.stacks;
        }
    }

}
