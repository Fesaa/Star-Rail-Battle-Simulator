package art.ameliah.hsr.characters.goal.shared.turn;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.AbstractTurnGoal;
import art.ameliah.hsr.characters.goal.TurnGoalResult;

public class AlwaysBasicGoal<C extends AbstractCharacter<C>> extends AbstractTurnGoal<C> {

    public AlwaysBasicGoal(C character) {
        super(character);
    }

    @Override
    public TurnGoalResult determineAction() {
        return TurnGoalResult.BASIC;
    }
}
