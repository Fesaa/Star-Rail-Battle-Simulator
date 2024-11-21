package art.ameliah.hsr.characters.goal.shared;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.Numby;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.UltGoal;
import art.ameliah.hsr.characters.topaz.Topaz;

import java.util.Map;

public class DontUltNumby<C extends AbstractCharacter<C>> extends UltGoal<C> {

    public DontUltNumby(C character) {
        super(character);
    }

    @Override
    public UltGoalResult determineAction() {
        if (!getBattle().hasCharacter(Topaz.NAME)) {
            return UltGoalResult.PASS;
        }


        for (Map.Entry<AbstractEntity,Float> entry : getBattle().getActionValueMap().entrySet()) {
            if (entry.getKey().name.equals(Numby.NAME)) {
                if (entry.getValue() <= 0) {
                    return UltGoalResult.DONT;
                }
            }
        }

        return UltGoalResult.PASS;
    }
}
