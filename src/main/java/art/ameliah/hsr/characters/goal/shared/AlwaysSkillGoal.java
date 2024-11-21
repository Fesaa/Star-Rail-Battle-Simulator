package art.ameliah.hsr.characters.goal.shared;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.TurnGoal;

public class AlwaysSkillGoal<C extends AbstractCharacter<C>> extends TurnGoal<C> {

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
