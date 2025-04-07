package art.ameliah.hsr.characters.harmony.robin;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.AbstractUltGoal;
import art.ameliah.hsr.characters.goal.UltGoalResult;


public class RobinDPSUltGoal extends AbstractUltGoal<Robin> {
    public RobinDPSUltGoal(Robin character) {
        super(character);
    }

    @Override
    public UltGoalResult determineAction() {
        AbstractCharacter<?> dpsChar = null;
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            if (character.isDPS) {
                dpsChar = character;
                break;
            }
        }
        if (dpsChar == null) {
            return UltGoalResult.PASS;
        }

        if (getBattle().getActionValueMap().get(dpsChar) < dpsChar.getBaseAV() * 0.7) {
            return UltGoalResult.DONT;
        } else {
            return UltGoalResult.DO;
        }
    }
}
