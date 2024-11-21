package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.List;

public class MusketeerOfWildWheat extends AbstractRelicSetBonus {
    public MusketeerOfWildWheat(AbstractCharacter<?> owner) {
        super(owner);
    }
    public MusketeerOfWildWheat(AbstractCharacter<?> owner, boolean isFullSet) {
        super(owner, isFullSet);
    }

    public void onEquip() {
        PermPower statBonus = new PermPower();
        statBonus.name = "Musketeer Stat Bonus";
        statBonus.setStat(PowerStat.ATK_PERCENT, 12);
        if (this.isFullSet) {
            statBonus.setStat(PowerStat.SPEED_PERCENT, 6);
        }
        owner.addPower(statBonus);
    }

    public void onCombatStart() {
        if (this.isFullSet) {
            owner.addPower(new MusketeerDamagePower());
        }
    }

    public String toString() {
        if (isFullSet) {
            return "4 PC Musketeer";
        } else {
            return "2 PC Musketeer";
        }
    }

    private static class MusketeerDamagePower extends AbstractPower {
        public MusketeerDamagePower() {
            this.name = this.getClass().getSimpleName();
            this.lastsForever = true;
        }
        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.BASIC) {
                    return 10;
                }
            }
            return 0;
        }
    }

}
