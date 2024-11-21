package art.ameliah.hsr.characters.topaz;

import art.ameliah.hsr.characters.goal.UltGoal;

public class TopazUltGoal extends UltGoal<Topaz> {
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
