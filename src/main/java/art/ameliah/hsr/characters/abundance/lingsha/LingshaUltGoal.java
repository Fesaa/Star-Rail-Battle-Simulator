package art.ameliah.hsr.characters.abundance.lingsha;

import art.ameliah.hsr.characters.goal.AbstractUltGoal;
import art.ameliah.hsr.characters.goal.UltGoalResult;

import static art.ameliah.hsr.characters.goal.UltGoalResult.DO;
import static art.ameliah.hsr.characters.goal.UltGoalResult.DONT;

public class LingshaUltGoal extends AbstractUltGoal<Lingsha> {
    public LingshaUltGoal(Lingsha character) {
        super(character);
    }

    @Override
    public UltGoalResult determineAction() {
        if (getBattle().getActionValueMap().get(this.character.fuYuan) >= this.character.fuYuan.getBaseAV() * 0.5) {
            return DO;
        }

        return DONT;
    }
}
