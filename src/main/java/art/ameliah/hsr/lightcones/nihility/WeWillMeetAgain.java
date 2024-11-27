package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.utils.Randf;

public class WeWillMeetAgain extends AbstractLightcone {

    public WeWillMeetAgain(AbstractCharacter<?> owner) {
        super(847, 529, 331, owner);
    }

    @Override
    public void beforeAttack(AttackLogic attack) {
        attack.hit(Randf.rand(attack.getTargets(), getBattle().getGetRandomEnemyRng()), 0.96f);
    }
}
