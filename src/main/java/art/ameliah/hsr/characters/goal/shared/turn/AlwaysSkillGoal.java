package art.ameliah.hsr.characters.goal.shared.turn;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.AbstractTurnGoal;
import art.ameliah.hsr.characters.goal.TurnGoalResult;

public class AlwaysSkillGoal<C extends AbstractCharacter<C>> extends AbstractTurnGoal<C> {

    private final int threshold;

    public AlwaysSkillGoal(C character) {
        this(character, 0);
    }

    public AlwaysSkillGoal(C character, int threshold) {
        super(character);
        this.threshold = threshold;
    }

    @Override
    public TurnGoalResult determineAction() {
        if (getBattle().getSkillPoints() > this.threshold) {
            return TurnGoalResult.SKILL;
        }

        return TurnGoalResult.BASIC;
    }
}
