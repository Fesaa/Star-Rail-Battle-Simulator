package art.ameliah.hsr.characters.goal.shared;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.TurnGoal;

/**
 * Goal to use a skill if the character has excess skill points. Returns PASS otherwise.
 */
public class UseExcessSkillPointsGoal<C extends AbstractCharacter<C>> extends TurnGoal<C> {

    private final int threshold;

    public UseExcessSkillPointsGoal(C character) {
        this(character, 4);
    }

    public UseExcessSkillPointsGoal(C character, int threshold) {
        super(character);
        this.threshold = threshold;
    }

    @Override
    public TurnGoalResult determineAction() {
        if (getBattle().getSkillPoints() >= this.threshold) {
            return TurnGoalResult.SKILL;
        }

        return TurnGoalResult.PASS;
    }
}
