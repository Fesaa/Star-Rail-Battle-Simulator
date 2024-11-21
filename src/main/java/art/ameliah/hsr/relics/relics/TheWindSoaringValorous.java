package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.ArrayList;

public class TheWindSoaringValorous extends AbstractRelicSetBonus {
    public TheWindSoaringValorous(AbstractCharacter<?> owner) {
        super(owner);
    }
    public TheWindSoaringValorous(AbstractCharacter<?> owner, boolean isFullSet) {
        super(owner, isFullSet);
    }

    public void onEquip() {
        PermPower statBonus = new PermPower();
        statBonus.name = "Valorous Stat Bonus";
        statBonus.setStat(PowerStat.ATK_PERCENT, 12);
        if (this.isFullSet) {
            statBonus.setStat(PowerStat.CRIT_CHANCE, 6);
        }
        owner.addPower(statBonus);
    }

    @Override
    public void onBeforeUseAttack(ArrayList<DamageType> damageTypes) {
        if (damageTypes.contains(DamageType.FOLLOW_UP) && isFullSet) {
            owner.addPower(new ValorousDamagePower());
        }
    }

    public String toString() {
        if (isFullSet) {
            return "4 PC Wind Soaring Valorous";
        } else {
            return "2 PC Wind Soaring Valorous";
        }
    }

    private static class ValorousDamagePower extends AbstractPower {
        public ValorousDamagePower() {
            this.name = this.getClass().getSimpleName();
            this.turnDuration = 1;
        }
        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.ULTIMATE) {
                    return 36;
                }
            }
            return 0;
        }
    }

}
