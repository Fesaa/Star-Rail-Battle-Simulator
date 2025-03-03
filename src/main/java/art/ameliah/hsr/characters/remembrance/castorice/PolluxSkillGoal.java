package art.ameliah.hsr.characters.remembrance.castorice;

import art.ameliah.hsr.characters.goal.AbstractTurnGoal;
import art.ameliah.hsr.characters.goal.TurnGoalResult;

public class PolluxSkillGoal extends AbstractTurnGoal<Pollux> {

    public PolluxSkillGoal(Pollux character) {
        super(character);
    }

    @Override
    public TurnGoalResult determineAction() {
        if (this.character.getTurns() % 3 == 0) {
            return TurnGoalResult.SKILL;
        }

        if (this.character.getCurrentHp().get() > 1) {
            return TurnGoalResult.SKILL;
        }

        return TurnGoalResult.BASIC;
    }
}
