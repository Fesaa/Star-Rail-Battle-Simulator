package art.ameliah.hsr.characters.goal;

@FunctionalInterface
public interface TurnGoal {
    TurnGoalResult determineAction();
}
