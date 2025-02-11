package art.ameliah.hsr.characters.harmony.robin;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.harmony.bronya.Bronya;
import art.ameliah.hsr.characters.hunt.feixiao.Feixiao;
import art.ameliah.hsr.characters.goal.UltGoal;


public class RobinBroynaFeixiaoUltGoal extends UltGoal<Robin> {
    public RobinBroynaFeixiaoUltGoal(Robin character) {
        super(character);
    }

    @Override
    public UltGoalResult determineAction() {
        if (!getBattle().hasCharacter(Feixiao.NAME)) {
            return UltGoalResult.PASS;
        }

        AbstractCharacter<?> bronya = getBattle().getCharacter(Bronya.NAME);
        if (bronya != null) {
            if (getBattle().getActionValueMap().get(bronya) < bronya.getBaseAV() * 0.7) {
                return UltGoalResult.DONT;
            } else {
                return UltGoalResult.DO;
            }
        }

        AbstractCharacter<?> feixiao = getBattle().getCharacter(Feixiao.NAME);
        if (feixiao != null) {
            if (getBattle().getActionValueMap().get(feixiao) < feixiao.getBaseAV() * 0.7) {
                return UltGoalResult.DONT;
            } else {
                return UltGoalResult.DO;
            }
        }

        return UltGoalResult.PASS;
    }
}
