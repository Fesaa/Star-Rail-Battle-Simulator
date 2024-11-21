package art.ameliah.hsr.lightcones.erudition;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

import java.util.ArrayList;
import java.util.Set;

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
            this.name = this.getClass().getSimpleName();
            this.lastsForever = true;
        }

        @Override
        public void onAttack(AbstractCharacter<?> character, Set<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
            this.stacks = Math.min(5, enemiesHit.size());
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
