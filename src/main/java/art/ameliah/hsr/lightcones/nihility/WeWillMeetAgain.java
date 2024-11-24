package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;

public class WeWillMeetAgain extends AbstractLightcone {

    public WeWillMeetAgain(AbstractCharacter<?> owner) {
        super(847, 529, 331, owner);
    }

    @Override
    public void onAttack(Attack attack) {
        for (AbstractEnemy enemy : attack.getTargets()) {
            attack.hitEnemy(this.owner, enemy, 0.96f, MultiplierStat.ATK);
        }
    }
}
