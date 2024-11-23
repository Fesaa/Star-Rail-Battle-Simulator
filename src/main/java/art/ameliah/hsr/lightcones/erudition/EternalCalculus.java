package art.ameliah.hsr.lightcones.erudition;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class EternalCalculus extends AbstractLightcone {

    public EternalCalculus(AbstractCharacter<?> owner) {
        super(1058, 529, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.ATK_PERCENT, 12, "Eternal Calculus Attack Boost"));
        this.owner.addPower(new EternalCalculusPower());
    }

    public static class EternalCalculusPower extends AbstractPower {

        public EternalCalculusPower() {
            this.setName(this.getClass().getSimpleName());
            this.lastsForever = true;
        }

        @Override
        public void onAttack(Attack attack) {
            this.stacks = Math.min(5, attack.getTargets().size());
            if (this.stacks > 3) {
                getBattle().IncreaseSpeed(this.owner, TempPower.create(PowerStat.SPEED_PERCENT, 16, 1, "Eternal Calculus Speed Boost"));
            }
        }

        @Override
        public float getConditionalAtkBonus(AbstractCharacter<?> character) {
            return 8 * this.stacks;
        }
    }
}
