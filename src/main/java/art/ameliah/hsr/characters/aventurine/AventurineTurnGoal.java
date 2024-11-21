package art.ameliah.hsr.characters.aventurine;

import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.goal.TurnGoal;

public class AventurineTurnGoal extends TurnGoal<Aventurine> {
    public AventurineTurnGoal(Aventurine character) {
        super(character);
    }

    @Override
    public TurnGoalResult determineAction() {
        if (!this.character.SPNeutral) {
            return TurnGoalResult.BASIC;
        }

        if (getBattle().getSkillPoints() == 0) {
            return TurnGoalResult.BASIC;
        }

        if (this.character.lastMove(MoveType.BASIC) || this.character.firstMove) {
            this.character.firstMove = false;
            return TurnGoalResult.SKILL;
        }

        return TurnGoalResult.BASIC;
    }
}
