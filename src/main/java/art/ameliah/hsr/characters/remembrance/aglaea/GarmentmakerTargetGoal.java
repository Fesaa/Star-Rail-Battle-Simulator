package art.ameliah.hsr.characters.remembrance.aglaea;

import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.goal.EnemyTargetGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;

import java.util.Optional;

public class GarmentmakerTargetGoal extends EnemyTargetGoal<Garmentmaker> {
    public GarmentmakerTargetGoal(Garmentmaker character) {
        super(character);
    }

    @Override
    public Optional<AbstractEnemy> getTarget(MoveType dmgType) {
        return getBattle()
                .getEnemies()
                .stream()
                .filter(e -> e.hasPower(Aglaea.SeamStitch.NAME))
                .findFirst();
    }
}
