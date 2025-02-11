package art.ameliah.hsr.characters.goal.shared.ult;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.UltGoal;

public class DontUltWhenClose<C extends AbstractCharacter<C>> extends UltGoal<C> {

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
