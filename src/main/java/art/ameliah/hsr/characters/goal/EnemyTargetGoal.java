package art.ameliah.hsr.characters.goal;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class EnemyTargetGoal<C extends AbstractCharacter<C>> implements BattleParticipant {

    protected final C character;

    public abstract Optional<AbstractEnemy> getTarget(MoveType dmgType);

    @Override
    public IBattle getBattle() {
        return this.character.getBattle();
    }
}
