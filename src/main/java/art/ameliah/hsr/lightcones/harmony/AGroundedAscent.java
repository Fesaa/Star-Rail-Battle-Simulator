package art.ameliah.hsr.lightcones.harmony;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Summoner;
import art.ameliah.hsr.characters.harmony.sunday.Sunday;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.TempPower;

import java.util.Collection;
import java.util.List;

public class AGroundedAscent extends AbstractLightcone {

    private int counter = 0;

    public AGroundedAscent(AbstractCharacter<?> owner) {
        super(1164, 476, 529, owner);
    }

    @Override
    public void useOnAlly(Collection<AbstractCharacter<?>> targets, MoveType action) {
        if (targets.size() != 1 || !(action.equals(MoveType.SKILL) || action.equals(MoveType.ULTIMATE))) {
            return;
        }
        this.counter++;
        var target = targets.stream().findFirst().get();

        this.owner.increaseEnergy(6, false, "A Grounded Ascent");
        target.addPower(new Hymn());
        if (this.owner instanceof Sunday && target instanceof Summoner summoner) {
            var summon = summoner.getSummon();
            if (summon != null) {
                summon.addPower(new Hymn());
            }
        }

        if (this.counter % 2 == 0) {
            getBattle().generateSkillPoint(this.owner, 1);
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
