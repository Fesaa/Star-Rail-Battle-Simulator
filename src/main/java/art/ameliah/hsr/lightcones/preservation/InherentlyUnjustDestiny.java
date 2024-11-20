package art.ameliah.hsr.lightcones.preservation;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import java.util.ArrayList;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

/**
 * The battle sim does not have a concept of shields, we assume 100% update.
 */
public class InherentlyUnjustDestiny extends AbstractLightcone {

    public InherentlyUnjustDestiny(AbstractCharacter<?> owner) {
        super(1058, 423, 662, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.DEF_PERCENT, 40, "Inherently Unjust Destiny Defense Boost"));
        this.owner.addPower(PermPower.create(PowerStat.CRIT_DAMAGE, 40, "Inherently Unjust Destiny Crit Damage Boost"));
    }

    @Override
    public void onAttack(AbstractCharacter<?> character, ArrayList<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
        if (!types.contains(DamageType.FOLLOW_UP)) return;

        // TODO: Take EHR into account
        for (AbstractEnemy enemy : enemiesHit) {
            enemy.addPower(new FollowDmgBonus());
        }
    }

    public static class FollowDmgBonus extends TempPower {
        public FollowDmgBonus() {
            super(2);
            this.name = this.getClass().getSimpleName();
            this.type = PowerType.DEBUFF;
            this.setStat(PowerStat.DAMAGE_TAKEN, 10);
        }
    }

}