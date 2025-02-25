package art.ameliah.hsr.lightcones.erudition;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreAllyAttack;
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

            this.setConditionalStat(PowerStat.ATK_PERCENT, _ -> 8f * this.stacks);
        }

        @Subscribe
        public void beforeAttack(PreAllyAttack e) {
            this.stacks = Math.min(5, e.getAttack().getTargets().size());
            if (this.stacks > 3) {
                getBattle().IncreaseSpeed(this.getOwner(), TempPower.create(PowerStat.SPEED_PERCENT, 16, 1, "Eternal Calculus Speed Boost"));
            }
        }
    }
}
