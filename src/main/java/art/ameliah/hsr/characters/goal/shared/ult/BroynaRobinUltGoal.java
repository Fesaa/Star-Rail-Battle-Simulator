package art.ameliah.hsr.characters.goal.shared.ult;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.UltGoal;
import art.ameliah.hsr.characters.harmony.bronya.Bronya;
import art.ameliah.hsr.characters.harmony.robin.Robin;

import static art.ameliah.hsr.characters.goal.UltGoal.UltGoalResult.DO;
import static art.ameliah.hsr.characters.goal.UltGoal.UltGoalResult.PASS;


public class BroynaRobinUltGoal<C extends AbstractCharacter<C>> extends UltGoal<C> {
    public BroynaRobinUltGoal(C character) {
        super(character);
    }

    @Override
    public UltGoalResult determineAction() {
        if (getBattle().hasCharacter(Bronya.NAME) && getBattle().hasCharacter(Robin.NAME)) {
            if (this.character.hasPower(Bronya.SKILL_POWER_NAME) && this.character.hasPower(Bronya.ULT_POWER_NAME)) {
                return DO;
            }
        }

        return PASS;
    }
}
