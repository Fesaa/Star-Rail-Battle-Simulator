package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.ArrayList;

public class LongevousDisciple extends AbstractRelicSetBonus {
    public LongevousDisciple(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public LongevousDisciple(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.HP_PERCENT, 12, "Longevous Disciple 2PC"));
    }

    @Override
    public void onAttacked(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> types, int energyFromAttacked, float totalDmg) {
        if (!this.isFullSet) return;

        // Assuming stuff like eating your own/allys HP, would be implemented as attacking them
        // No char currently does it, but this feels logical. Otherwise, need a hook onHPChange hook
        this.owner.addPower(new LongevousDisciple4PC());
    }

    public static class LongevousDisciple4PC extends TempPower {
        public LongevousDisciple4PC() {
            super(2, "Longevous Disciple 4PC");
            this.maxStacks = 2;
        }

        @Override
        public float getConditionalCritRate(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            return this.stacks * 8;
        }
    }
}
