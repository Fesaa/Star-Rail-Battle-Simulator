package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreAllyAttack;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.List;

public class TheWindSoaringValorous extends AbstractRelicSetBonus {
    public TheWindSoaringValorous(AbstractCharacter<?> owner) {
        super(owner);
    }

    public TheWindSoaringValorous(AbstractCharacter<?> owner, boolean isFullSet) {
        super(owner, isFullSet);
    }

    public void onEquip() {
        PermPower statBonus = new PermPower();
        statBonus.setName("Valorous Stat Bonus");
        statBonus.setStat(PowerStat.ATK_PERCENT, 12);
        if (this.isFullSet) {
            statBonus.setStat(PowerStat.CRIT_CHANCE, 6);
        }
        owner.addPower(statBonus);
    }

    @Subscribe
    public void beforeAttack(PreAllyAttack event) {
        if (event.getAttack().getTypes().contains(DamageType.FOLLOW_UP) && isFullSet) {
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

    public static class ValorousDamagePower extends AbstractPower {
        public ValorousDamagePower() {
            this.setName(this.getClass().getSimpleName());
            this.turnDuration = 1;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.ULTIMATE) {
                    return 36;
                }
            }
            return 0;
        }
    }

}
