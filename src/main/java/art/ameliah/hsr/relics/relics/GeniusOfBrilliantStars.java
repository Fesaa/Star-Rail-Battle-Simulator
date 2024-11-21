package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.List;

public class GeniusOfBrilliantStars extends AbstractRelicSetBonus {
    public GeniusOfBrilliantStars(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public GeniusOfBrilliantStars(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.QUANTUM_DMG_BOOST, 10, "Genius of Brilliant Stars Quantum bonus"));

        if (this.isFullSet) {
            this.owner.addPower(new GeniusOfBrilliantStars4PC());
        }
    }

    public static class GeniusOfBrilliantStars4PC extends PermPower {
        public GeniusOfBrilliantStars4PC() {
            super("Genius of Brilliant Stars 4PC power");
        }

        @Override
        public float getConditionDefenseIgnore(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (enemy.hasWeakness(ElementType.QUANTUM)) {
                return 10 * 2;
            }

            return 10;
        }
    }
}
