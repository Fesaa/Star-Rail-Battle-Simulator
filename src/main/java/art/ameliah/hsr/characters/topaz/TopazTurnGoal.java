package art.ameliah.hsr.characters.topaz;

import art.ameliah.hsr.characters.goal.TurnGoal;

public class TopazTurnGoal extends TurnGoal<Topaz> {
    public TopazTurnGoal(Topaz character) {
        super(character);
    }

    @Override
    public TurnGoalResult determineAction() {
        if (getBattle().getSkillPoints() <= 3 || character.ultCounter > 0) {
            return TurnGoalResult.BASIC;
        }

        return TurnGoalResult.PASS;
    }
}
