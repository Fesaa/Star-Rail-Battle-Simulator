package art.ameliah.hsr.characters.goal.shared;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.TurnGoal;

public class SkillFirstTurnGoal<C extends AbstractCharacter<C> & SkillFirstTurnGoal.FirstTurnTracked> extends TurnGoal<C> {
    public SkillFirstTurnGoal(C character) {
        super(character);
    }

    @Override
    public TurnGoalResult determineAction() {
        if (getBattle().getSkillPoints() > 0 && this.character.isFirstTurn()) {
            this.character.setFirstTurn(false);
            return TurnGoalResult.SKILL;
        }

        return TurnGoalResult.PASS;
    }

    public interface FirstTurnTracked {
        boolean isFirstTurn();

        void setFirstTurn(boolean firstTurn);
    }
}
