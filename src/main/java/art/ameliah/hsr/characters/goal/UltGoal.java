package art.ameliah.hsr.characters.goal;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.characters.AbstractCharacter;

public abstract class UltGoal<C extends AbstractCharacter<C>> implements BattleParticipant {

    protected final C character;

    public UltGoal(C character) {
        this.character = character;
    }

    public abstract UltGoalResult determineAction();

    @Override
    public IBattle getBattle() {
        return this.character.getBattle();
    }

    public enum UltGoalResult {
        DO,
        DONT,
        PASS
    }
}
