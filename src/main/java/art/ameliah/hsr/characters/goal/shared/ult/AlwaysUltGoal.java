package art.ameliah.hsr.characters.goal.shared.ult;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.AbstractUltGoal;
import art.ameliah.hsr.characters.goal.UltGoalResult;

public class AlwaysUltGoal<C extends AbstractCharacter<C>> extends AbstractUltGoal<C> {
    public AlwaysUltGoal(C character) {
        super(character);
    }

    @Override
    public UltGoalResult determineAction() {
        return UltGoalResult.DO;
    }
}
