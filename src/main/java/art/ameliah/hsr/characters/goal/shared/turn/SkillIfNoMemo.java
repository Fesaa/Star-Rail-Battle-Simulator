package art.ameliah.hsr.characters.goal.shared.turn;

import art.ameliah.hsr.characters.goal.AbstractTurnGoal;
import art.ameliah.hsr.characters.goal.TurnGoalResult;
import art.ameliah.hsr.characters.remembrance.Memomaster;

public class SkillIfNoMemo<C extends Memomaster<C>> extends AbstractTurnGoal<C> {
    public SkillIfNoMemo(C character) {
        super(character);
    }

    @Override
    public TurnGoalResult determineAction() {
        var memo = this.character.getMemo();
        if (memo == null) {
            return TurnGoalResult.SKILL;
        }
        return TurnGoalResult.PASS;
    }
}
