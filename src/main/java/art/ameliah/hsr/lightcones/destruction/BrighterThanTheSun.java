package art.ameliah.hsr.lightcones.destruction;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreAllyAttack;
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

    @Subscribe
    public void beforeAttack(PreAllyAttack e) {
        if (e.getAttack().getTypes().contains(DamageType.BASIC)) {
            this.owner.addPower(new DragonsCall());
        }
    }

    public static class DragonsCall extends TempPower {
        public DragonsCall() {
            this.setName(this.getClass().getSimpleName());
            this.turnDuration = 2;
            this.maxStacks = 2;

            this.setConditionalStat(PowerStat.ATK_PERCENT, _ -> 18f * this.stacks);
            this.setConditionalStat(PowerStat.ENERGY_REGEN, _ -> 6f * this.stacks);
        }
    }

}
