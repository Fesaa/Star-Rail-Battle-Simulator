package art.ameliah.hsr.relics.ornament;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.List;

public class InertSalsotto extends AbstractRelicSetBonus {
    public InertSalsotto(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public InertSalsotto(AbstractCharacter<?> owner) {
        super(owner);
    }

    public void onEquip() {
        owner.addPower(PermPower.create(PowerStat.CRIT_CHANCE, 8, "Inert Salsotto Crit Chance Bonus"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent e) {
        owner.addPower(new InertSalsottoDamagePower());
    }

    public static class InertSalsottoDamagePower extends AbstractPower {
        public InertSalsottoDamagePower() {
            this.setName(this.getClass().getSimpleName());
            this.lastsForever = true;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.FOLLOW_UP || type == DamageType.ULTIMATE) {
                    return 15;
                }
            }
            return 0;
        }
    }

}
