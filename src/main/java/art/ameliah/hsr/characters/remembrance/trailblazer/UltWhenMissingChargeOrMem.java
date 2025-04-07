package art.ameliah.hsr.characters.remembrance.trailblazer;

import art.ameliah.hsr.characters.goal.AbstractUltGoal;
import art.ameliah.hsr.characters.goal.UltGoalResult;

public class UltWhenMissingChargeOrMem extends AbstractUltGoal<Trailblazer> {

    public UltWhenMissingChargeOrMem(Trailblazer character) {
        super(character);
    }

    @Override
    public UltGoalResult determineAction() {
        if (character.getMemo() == null) {
            return UltGoalResult.DO;
        }

        if (((Mem) character.getMemo()).charge.get() > 60) {
            return UltGoalResult.DONT;
        }

        return UltGoalResult.PASS;
    }
}
