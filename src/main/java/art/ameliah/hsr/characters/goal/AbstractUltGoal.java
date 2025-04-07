package art.ameliah.hsr.characters.goal;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.characters.AbstractCharacter;

public abstract class AbstractUltGoal<C extends AbstractCharacter<C>> implements BattleParticipant, UltGoal {

    protected final C character;

    public AbstractUltGoal(C character) {
        this.character = character;
    }

    @Override
    public IBattle getBattle() {
        return this.character.getBattle();
    }

}
