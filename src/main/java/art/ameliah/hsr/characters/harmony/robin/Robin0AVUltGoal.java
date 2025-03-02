package art.ameliah.hsr.characters.harmony.robin;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.AbstractUltGoal;
import art.ameliah.hsr.characters.goal.UltGoalResult;

import java.util.Map;

public class Robin0AVUltGoal extends AbstractUltGoal<Robin> {
    public Robin0AVUltGoal(Robin character) {
        super(character);
    }

    @Override
    public UltGoalResult determineAction() {
        for (Map.Entry<AbstractEntity, Float> entry : getBattle().getActionValueMap().entrySet()) {
            if (entry.getKey() instanceof AbstractCharacter && entry.getValue() <= 0) {
                return UltGoalResult.DONT;
            }
        }

        return UltGoalResult.PASS;
    }
}
