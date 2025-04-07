package art.ameliah.hsr.characters.goal.shared.ult;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.AbstractUltGoal;
import art.ameliah.hsr.characters.goal.UltGoalResult;

public class DontUltWhenClose<C extends AbstractCharacter<C>> extends AbstractUltGoal<C> {

    private final float per;

    /**
     * Returns UltGoalResult.DONT if there is less than baseAV * per AV remaining
     */
    public DontUltWhenClose(C character, float per) {
        super(character);

        this.per = per;
    }

    @Override
    public UltGoalResult determineAction() {
        Float av = getBattle().getActionValueMap().get(this.character);
        if (av == null) {
            return UltGoalResult.PASS;
        }

        if (this.character.getBaseAV() * per < av) {
            return UltGoalResult.DONT;
        }

        return UltGoalResult.PASS;
    }
}
