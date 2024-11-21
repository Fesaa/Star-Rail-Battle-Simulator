package art.ameliah.hsr.characters.goal.shared;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.UltGoal;

public class AlwaysUltGoal<C extends AbstractCharacter<C>> extends UltGoal<C> {
    public AlwaysUltGoal(C character) {
        super(character);
    }

    @Override
    public UltGoalResult determineAction() {
        return UltGoalResult.DO;
    }
}
