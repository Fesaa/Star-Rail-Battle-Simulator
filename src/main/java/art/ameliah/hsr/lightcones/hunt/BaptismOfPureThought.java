package art.ameliah.hsr.lightcones.hunt;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

import java.util.List;

/**
 * enemyDebugs are not tracked, so it a constant. It is capped at 3. Passing none assumes 3
 */
public class BaptismOfPureThought extends AbstractLightcone {

    private final int enemyDebugs;

    public BaptismOfPureThought(AbstractCharacter<?> owner) {
        this(owner, 3);
    }

    public BaptismOfPureThought(AbstractCharacter<?> owner, int enemyDebugs) {
        super(953, 582, 529, owner);
        this.enemyDebugs = Math.min(3, enemyDebugs);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_DAMAGE, 20 + this.enemyDebugs * 8, "Baptism Of Pure Thought Crit Damage Boost"));
    }

    @Override
    public void onUseUltimate() {
        this.owner.addPower(new DisputationEffect());
    }

    public static class DisputationEffect extends TempPower {
        public DisputationEffect() {
            this.name = this.getClass().getSimpleName();
            this.turnDuration = 2;
            this.setStat(PowerStat.DAMAGE_BONUS, 36);
        }

        @Override
        public float getConditionDefenseIgnore(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (!damageTypes.contains(DamageType.FOLLOW_UP)) return 0;

            return 24;
        }
    }
}
