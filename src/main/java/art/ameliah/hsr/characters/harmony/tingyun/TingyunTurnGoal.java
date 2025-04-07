package art.ameliah.hsr.characters.harmony.tingyun;

import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.goal.AbstractTurnGoal;
import art.ameliah.hsr.characters.goal.TurnGoalResult;

public class TingyunTurnGoal extends AbstractTurnGoal<Tingyun> {

    public TingyunTurnGoal(Tingyun character) {
        super(character);
    }

    @Override
    public TurnGoalResult determineAction() {
        if (getBattle().getSkillPoints() > 0) {
            boolean moveGood = (character.lastMove(MoveType.BASIC) && character.lastMoveBefore(MoveType.BASIC));
            if (character.getBenefactor() == null || moveGood) {
                return TurnGoalResult.SKILL;
            }
        }

        return TurnGoalResult.BASIC;
    }
}
