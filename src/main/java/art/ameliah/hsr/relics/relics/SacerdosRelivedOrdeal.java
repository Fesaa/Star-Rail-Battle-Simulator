package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.Collection;
import java.util.List;

public class SacerdosRelivedOrdeal extends AbstractRelicSetBonus {
    public SacerdosRelivedOrdeal(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public SacerdosRelivedOrdeal(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.SPEED_PERCENT, 6, "Sacerdos' Relived Ordeal SPD Boost"));
    }

    @Override
    public void useOnAlly(Collection<AbstractCharacter<?>> targets, MoveType action) {
        if (targets.size() != 1 || !this.isFullSet) {
            return;
        }

        AbstractCharacter<?> target = targets.iterator().next();
        target.addPower(new SacerdosMelodicEarrings());
    }

    public static class SacerdosMelodicEarrings extends TempPower {

        public static final String NAME = "Sacerdos Melodic Earrings";

        public SacerdosMelodicEarrings() {
            super(2, NAME);
            this.maxStacks = 2;
        }

        @Override
        public float getConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            return this.stacks * 18;
        }
    }
}
