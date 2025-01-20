package art.ameliah.hsr.enemies.game.jarilovi;

import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyType;

// https://homdgcat.wiki/sr/monster#_803201002
public class GruntFieldPersonnel extends AbstractEnemy implements SeniorStaffTeamLeader.Grunt {
    public GruntFieldPersonnel(int baseHP, int baseATK, int baseDEF, float baseSpeed) {
        super("Grunt: Field Personnel", EnemyType.Minion, baseHP, baseATK, baseDEF, baseSpeed, 20, 95);
    }

    @Override
    protected void act() {

    }
}
