package art.ameliah.hsr.characters.hunt.topaz;

import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.goal.EnemyTargetGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;

import java.util.Optional;

public class TopazTargetGoal extends EnemyTargetGoal<Topaz> {

    public TopazTargetGoal(Topaz character) {
        super(character);
    }

    @Override
    public Optional<AbstractEnemy> getTarget(MoveType type) {
        // TODO: If skill, maybe change targets to highest hp?

        return getBattle().getEnemies()
                .stream()
                .filter(e -> e.hasPower(Topaz.ProofOfDebt.NAME))
                .findFirst();
    }
}
