package art.ameliah.hsr.characters.abundance.lingsha;

import art.ameliah.hsr.characters.goal.UltGoal;

import static art.ameliah.hsr.characters.goal.UltGoal.UltGoalResult.DO;
import static art.ameliah.hsr.characters.goal.UltGoal.UltGoalResult.DONT;

public class LingshaUltGoal extends UltGoal<Lingsha> {
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
