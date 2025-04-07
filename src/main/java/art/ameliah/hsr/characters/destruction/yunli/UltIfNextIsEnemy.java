package art.ameliah.hsr.characters.destruction.yunli;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.characters.goal.AbstractUltGoal;
import art.ameliah.hsr.characters.goal.UltGoalResult;
import art.ameliah.hsr.enemies.AbstractEnemy;

public class UltIfNextIsEnemy extends AbstractUltGoal<Yunli> {
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
