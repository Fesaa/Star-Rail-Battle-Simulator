package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.ArrayList;

public class PrisonerInDeepConfinement extends AbstractRelicSetBonus {
    public PrisonerInDeepConfinement(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public PrisonerInDeepConfinement(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.ATK_PERCENT, 12, "Prisoner In Deep Confinement 2PC"));

        if (this.isFullSet) {
            this.owner.addPower(new PrisonerInDeepConfinement4PC());
        }
    }

    public static class PrisonerInDeepConfinement4PC extends PermPower {
        public PrisonerInDeepConfinement4PC() {
            super("Prisoner In Deep Confinement 4PC");
        }

        @Override
        public float getConditionDefenseIgnore(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            int mul = (int) Math.min(3, enemy.powerList
                    .stream()
                    .filter(p -> p.type == PowerType.DOT)
                    .count());
            return mul * 6;
        }
    }
}
