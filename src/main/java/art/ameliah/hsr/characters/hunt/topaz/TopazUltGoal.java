package art.ameliah.hsr.characters.hunt.topaz;

import art.ameliah.hsr.characters.goal.AbstractUltGoal;
import art.ameliah.hsr.characters.goal.UltGoalResult;

public class TopazUltGoal extends AbstractUltGoal<Topaz> {
    public TopazUltGoal(Topaz character) {
        super(character);
    }

    @Override
    public UltGoalResult determineAction() {
        if (getBattle().getActionValueMap().get(character.numby) >= character.numby.getBaseAV() * 0.25) {
            return UltGoalResult.PASS;
        }

        return UltGoalResult.DONT;
    }
}
