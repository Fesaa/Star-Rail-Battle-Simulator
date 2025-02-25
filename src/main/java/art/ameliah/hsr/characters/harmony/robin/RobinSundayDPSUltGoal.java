package art.ameliah.hsr.characters.harmony.robin;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.UltGoal;
import art.ameliah.hsr.characters.harmony.sunday.Sunday;


public class RobinSundayDPSUltGoal extends UltGoal<Robin> {
    public RobinSundayDPSUltGoal(Robin character) {
        super(character);
    }

    @Override
    public UltGoalResult determineAction() {
        AbstractCharacter<?> sunday = getBattle().getCharacter(Sunday.NAME);
        if (sunday != null) {
            if (getBattle().getActionValueMap().get(sunday) < sunday.getBaseAV() * 0.7) {
                return UltGoalResult.DONT;
            } else {
                return UltGoalResult.DO;
            }
        }

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
