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

public class RutilentArena extends AbstractRelicSetBonus {
    public RutilentArena(AbstractCharacter<?> owner) {
        super(owner);
    }

    public void onEquip() {
        owner.addPower(PermPower.create(PowerStat.CRIT_CHANCE, 8, "Rutilent Arena Crit Chance Bonus"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent e) {
        owner.addPower(new RutilentArenaDamageBonus());
    }

    public static class RutilentArenaDamageBonus extends AbstractPower {
        public RutilentArenaDamageBonus() {
            this.setName(this.getClass().getSimpleName());
            this.lastsForever = true;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.BASIC || type == DamageType.SKILL) {
                    return 20;
                }
            }
            return 0;
        }
    }

}
