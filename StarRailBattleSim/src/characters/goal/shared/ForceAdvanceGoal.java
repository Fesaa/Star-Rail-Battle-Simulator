package characters.goal.shared;

import characters.AbstractCharacter;
import characters.goal.TurnGoal;

public class ForceAdvanceGoal<C extends AbstractCharacter<C> & ForceAdvanceGoal.Advancer> extends TurnGoal<C> {

    private final String target;

    public ForceAdvanceGoal(C character, String target) {
        super(character);
        this.target = target;
    }

    @Override
    public TurnGoalResult determineAction() {
        AbstractCharacter<?> target = getBattle().getCharacter(this.target);
        if (target == null) {
            return TurnGoalResult.PASS;
        }

        if (getBattle().getSkillPoints() > 2) {
            if (target.currentEnergy + target.skillEnergyGain >= target.ultCost) {
                this.character.setNextAdvance(target);
                return TurnGoalResult.SKILL;
            }
        }

        if (target.currentEnergy + target.basicEnergyGain >= target.ultCost) {
            this.character.setNextAdvance(target);
            return TurnGoalResult.SKILL;
        }

        return TurnGoalResult.PASS;
    }

    public interface Advancer {
        void setNextAdvance(AbstractCharacter<?> nextAdvance);
    }
}
