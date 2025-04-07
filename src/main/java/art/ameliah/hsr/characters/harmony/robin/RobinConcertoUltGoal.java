package art.ameliah.hsr.characters.harmony.robin;

import art.ameliah.hsr.characters.goal.AbstractUltGoal;
import art.ameliah.hsr.characters.goal.UltGoalResult;

public class RobinConcertoUltGoal extends AbstractUltGoal<Robin> {
    public RobinConcertoUltGoal(Robin character) {
        super(character);
    }

    @Override
    public UltGoalResult determineAction() {
        if (getBattle().getActionValueMap().containsKey(character.concerto)) {
            return UltGoalResult.DONT;
        }

        return UltGoalResult.PASS;
    }
}
