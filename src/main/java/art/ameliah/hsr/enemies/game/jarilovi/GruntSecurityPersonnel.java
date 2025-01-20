package art.ameliah.hsr.enemies.game.jarilovi;

import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyType;

// https://homdgcat.wiki/sr/monster#_803202002
public class GruntSecurityPersonnel extends AbstractEnemy implements SeniorStaffTeamLeader.Grunt {
    public GruntSecurityPersonnel(int baseHP, int baseATK, int baseDEF, float baseSpeed) {
        super("Grunt: Security Personnel", EnemyType.Minion, baseHP, baseATK, baseDEF, baseSpeed, 30, 95);
    }

    @Override
    protected void act() {

    }
}
