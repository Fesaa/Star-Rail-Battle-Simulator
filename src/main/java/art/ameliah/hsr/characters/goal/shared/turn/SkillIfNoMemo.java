package art.ameliah.hsr.characters.goal.shared.turn;

import art.ameliah.hsr.characters.goal.TurnGoal;
import art.ameliah.hsr.characters.remembrance.Memomaster;

public class SkillIfNoMemo<C extends Memomaster<C>> extends TurnGoal<C> {
    public SkillIfNoMemo(C character) {
        super(character);
    }

    @Override
    public TurnGoalResult determineAction() {
        var memo = this.character.getMemo();
        if (memo == null) {
            return TurnGoalResult.SKILL;
        }
        return TurnGoalResult.BASIC;
    }
}
