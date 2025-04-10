package art.ameliah.hsr.characters.erudition.theherta;

import art.ameliah.hsr.characters.goal.AbstractUltGoal;
import art.ameliah.hsr.characters.goal.UltGoalResult;

public class HertaUltGoal extends AbstractUltGoal<TheHerta> {

    public HertaUltGoal(TheHerta character) {
        super(character);
    }

    @Override
    public UltGoalResult determineAction() {
        if (getBattle().getActionValueMap().get(this.character) < this.character.getBaseAV() * 0.25f) {
            return UltGoalResult.DONT;
        }

        return UltGoalResult.PASS;
    }
}
