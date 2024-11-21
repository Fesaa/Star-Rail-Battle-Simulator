package art.ameliah.hsr.relics.ornament;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import java.util.ArrayList;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class RutilentArena extends AbstractRelicSetBonus {
    public RutilentArena(AbstractCharacter<?> owner) {
        super(owner);
    }
    public void onEquip() {
        owner.addPower(PermPower.create(PowerStat.CRIT_CHANCE, 8, "Rutilent Arena Crit Chance Bonus"));
    }

    public void onCombatStart() {
        owner.addPower(new RutilentArenaDamageBonus());
    }

    private static class RutilentArenaDamageBonus extends AbstractPower {
        public RutilentArenaDamageBonus() {
            this.name = this.getClass().getSimpleName();
            this.lastsForever = true;
        }
        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.BASIC || type == DamageType.SKILL) {
                    return 20;
                }
            }
            return 0;
        }
    }

}
