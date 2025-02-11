package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.TurnGoal;

public class TurnDecision implements Loggable {

    private final AbstractCharacter<?> character;
    private final Class<?> turnGoal;
    private final TurnGoal.TurnGoalResult result;

    public TurnDecision(AbstractCharacter<?> character, Class<?> turnGoal, TurnGoal.TurnGoalResult result) {
        this.character = character;
        this.turnGoal = turnGoal;
        this.result = result;
    }

    @Override
    public String asString() {
        return character.getName() + " decided to " + result + " because " + turnGoal.getSimpleName();
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
