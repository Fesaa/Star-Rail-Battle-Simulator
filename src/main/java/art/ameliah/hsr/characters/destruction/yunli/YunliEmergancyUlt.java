package art.ameliah.hsr.characters.destruction.yunli;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.characters.goal.UltGoal;

public class YunliEmergancyUlt extends UltGoal<Yunli> {
    public YunliEmergancyUlt(Yunli character) {
        super(character);
    }

    @Override
    public UltGoalResult determineAction() {
        AbstractEntity first = getBattle().getNextUnit(0);
        float firstAV = getBattle().getActionValueMap().get(first);

        AbstractEntity second = getBattle().getNextUnit(1);
        float secondAV = getBattle().getActionValueMap().get(second);

        if (secondAV > getBattle().battleLength() && firstAV < getBattle().battleLength()) {
            return UltGoalResult.DO;
        }

        return UltGoalResult.PASS;
    }
}
