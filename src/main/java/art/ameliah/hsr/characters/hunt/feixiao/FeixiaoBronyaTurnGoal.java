package art.ameliah.hsr.characters.hunt.feixiao;

import art.ameliah.hsr.characters.goal.AbstractTurnGoal;
import art.ameliah.hsr.characters.goal.TurnGoalResult;
import art.ameliah.hsr.characters.harmony.bronya.Bronya;

public class FeixiaoBronyaTurnGoal extends AbstractTurnGoal<Feixiao> {
    public FeixiaoBronyaTurnGoal(Feixiao character) {
        super(character);
    }

    @Override
    public TurnGoalResult determineAction() {
        if (!getBattle().hasCharacter(Bronya.NAME)) {
            return TurnGoalResult.PASS;
        }

        if (!this.character.hasPower(Bronya.SKILL_POWER_NAME)) {
            if (getBattle().getSkillPoints() >= 4) {
                return TurnGoalResult.SKILL;
            }
            return TurnGoalResult.BASIC;
        }

        if (getBattle().getSkillPoints() > 1) {
            return TurnGoalResult.SKILL;
        }
        return TurnGoalResult.BASIC;
    }
}
