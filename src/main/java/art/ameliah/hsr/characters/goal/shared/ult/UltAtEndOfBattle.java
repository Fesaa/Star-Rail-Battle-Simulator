package art.ameliah.hsr.characters.goal.shared.ult;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.UltGoal;
import art.ameliah.hsr.characters.robin.Robin;

public class UltAtEndOfBattle<C extends AbstractCharacter<C>> extends UltGoal<C> {
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

        if (robin.currentEnergy >= robin.maxEnergy && !this.character.name.equals(Robin.NAME)) {
            return UltGoalResult.DONT;
        }

        return UltGoalResult.DO;
    }
}
