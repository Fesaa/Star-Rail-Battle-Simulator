package art.ameliah.hsr.characters.hunt.feixiao;

import art.ameliah.hsr.characters.goal.TurnGoal;
import art.ameliah.hsr.characters.harmony.bronya.Bronya;

public class FeixiaoBronyaTurnGoal extends TurnGoal<Feixiao> {
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
