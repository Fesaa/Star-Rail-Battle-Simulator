package art.ameliah.hsr.lightcones.harmony;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.TempPower;

import java.util.Collection;
import java.util.List;

public class AGroundedAscent extends AbstractLightcone {
    public AGroundedAscent(AbstractCharacter<?> owner) {
        super(1164, 476, 529, owner);
    }

    @Override
    public void useOnAlly(Collection<AbstractCharacter<?>> targets, MoveType action) {
        if (targets.size() != 1 || !(action.equals(MoveType.SKILL) || action.equals(MoveType.ULTIMATE))) {
            return;
        }

        this.owner.increaseEnergy(6, false, "A Grounded Ascent");

        if (action.equals(MoveType.ULTIMATE)) {
            if (this.owner.getActionMetric().frequency(MoveType.ULTIMATE) % 2 == 1) {
                getBattle().generateSkillPoint(this.owner, 1);
            }
        }
    }

    public static class Hymn extends TempPower {

        public static final String NAME = "Hymn";

        public Hymn() {
            super(3, NAME);
            this.maxStacks = 3;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            return 15 * this.stacks;
        }
    }

}
