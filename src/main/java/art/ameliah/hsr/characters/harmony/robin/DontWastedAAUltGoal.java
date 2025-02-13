package art.ameliah.hsr.characters.harmony.robin;

import art.ameliah.hsr.characters.goal.UltGoal;

public class DontWastedAAUltGoal extends UltGoal<Robin> {
    public DontWastedAAUltGoal(Robin character) {
        super(character);
    }

    @Override
    public UltGoalResult determineAction() {

        var actsSoon = getBattle().getActionValueMap().entrySet().stream()
                .anyMatch(e -> e.getValue() < e.getKey().getBaseAV() * 0.05f);

        if (actsSoon) {
            return UltGoalResult.DONT;
        }

        return UltGoalResult.PASS;
    }
}
