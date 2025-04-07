package art.ameliah.hsr.lightcones.destruction;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreUltimate;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.List;

public class DanceAtSunset extends AbstractLightcone {

    public DanceAtSunset(AbstractCharacter<?> owner) {
        super(1058, 582, 463, owner);
    }

    @Override
    public void onEquip() {
        PermPower statBonus = new PermPower();
        statBonus.setName("Dance At Sunset Stat Bonus");
        statBonus.setStat(PowerStat.CRIT_DAMAGE, 36);
        statBonus.setStat(PowerStat.TAUNT_VALUE, 500);
        owner.addPower(statBonus);
    }

    @Subscribe
    public void onUseUltimate(PreUltimate e) {
        owner.addPower(new DanceAtSunsetDamagePower());
    }

    public static class DanceAtSunsetDamagePower extends AbstractPower {
        public DanceAtSunsetDamagePower() {
            this.setName(this.getClass().getSimpleName());
            this.turnDuration = 2;
            this.maxStacks = 2;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.FOLLOW_UP) {
                    return 36 * stacks;
                }
            }
            return 0;
        }
    }
}