package art.ameliah.hsr.characters.remembrance.castorice;

import art.ameliah.hsr.characters.goal.AbstractTurnGoal;
import art.ameliah.hsr.characters.goal.TurnGoalResult;

public class SkillOnBattleEnd extends AbstractTurnGoal<Pollux> {
    public SkillOnBattleEnd(Pollux character) {
        super(character);
    }

    @Override
    public TurnGoalResult determineAction() {
        return getBattle().isAboutToEnd() ? TurnGoalResult.SKILL : TurnGoalResult.PASS;
    }
}
