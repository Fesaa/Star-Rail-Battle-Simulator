package art.ameliah.hsr.lightcones.destruction;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import java.util.ArrayList;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

/**
 * Ignored DMG boost from break
 */
public class OnTheFallOfAnAeon extends AbstractLightcone {

    public OnTheFallOfAnAeon(AbstractCharacter<?> owner) {
        super(1058, 529, 397, owner);
    }

    @Override
    public void onAttack(AbstractCharacter<?> character, ArrayList<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
        this.owner.addPower(new OnTheFallOfAnAeonATKBoost());
    }

    public static class OnTheFallOfAnAeonATKBoost extends PermPower {
        public OnTheFallOfAnAeonATKBoost() {
            this.name = this.getClass().getSimpleName();
            this.maxStacks = 4;
            this.setStat(PowerStat.ATK_PERCENT, 16);
        }


    }
}
