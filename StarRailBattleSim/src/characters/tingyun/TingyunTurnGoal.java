package characters.tingyun;

import characters.MoveType;
import characters.goal.TurnGoal;

public class TingyunTurnGoal extends TurnGoal<Tingyun> {

    public TingyunTurnGoal(Tingyun character) {
        super(character);
    }

    @Override
    public TurnGoalResult determineAction() {
        if (getBattle().getSkillPoints() > 0) {
            boolean moveGood = (character.lastMove(MoveType.BASIC) && character.lastMoveBefore(MoveType.BASIC));
            if (character.benefactor == null || moveGood) {
                return TurnGoalResult.SKILL;
            }
        }

        return TurnGoalResult.BASIC;
    }
}
