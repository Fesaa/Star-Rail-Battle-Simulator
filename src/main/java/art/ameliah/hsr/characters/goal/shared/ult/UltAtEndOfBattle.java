package art.ameliah.hsr.characters.goal.shared.ult;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.AbstractUltGoal;
import art.ameliah.hsr.characters.goal.UltGoalResult;
import art.ameliah.hsr.characters.harmony.robin.Robin;

public class UltAtEndOfBattle<C extends AbstractCharacter<C>> extends AbstractUltGoal<C> {
    public UltAtEndOfBattle(C character) {
        super(character);
    }

    @Override
    public UltGoalResult determineAction() {
        if (!getBattle().isAboutToEnd()) {
            return UltGoalResult.PASS;
        }
        AbstractCharacter<?> robin = getBattle().getCharacter(Robin.NAME);
        if (robin == null) {
            return UltGoalResult.DO;
        }

        if (robin.getCurrentEnergy().get() >= robin.maxEnergy && !this.character.getName().equals(Robin.NAME)) {
            return UltGoalResult.DONT;
        }

        return UltGoalResult.DO;
    }
}
