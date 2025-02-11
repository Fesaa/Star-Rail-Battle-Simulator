package art.ameliah.hsr.characters.goal;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.characters.AbstractCharacter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class AllyTargetGoal<C extends AbstractCharacter<C>> implements BattleParticipant {

    protected final C character;

    public abstract Optional<AbstractCharacter<?>> getTarget();

    @Override
    public IBattle getBattle() {
        return this.character.getBattle();
    }
}
