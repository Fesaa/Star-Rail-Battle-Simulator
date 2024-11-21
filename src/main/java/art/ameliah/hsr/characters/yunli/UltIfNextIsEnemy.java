package art.ameliah.hsr.characters.yunli;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.characters.goal.UltGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;

public class UltIfNextIsEnemy extends UltGoal<Yunli> {
    public UltIfNextIsEnemy(Yunli character) {
        super(character);
    }

    @Override
    public UltGoalResult determineAction() {
        AbstractEntity next = getBattle().getCurrentUnit();
        if (next instanceof AbstractEnemy) {
            return UltGoalResult.DO;
        }

        return UltGoalResult.DONT;
    }
}
