package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreAllyAttack;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.utils.Randf;

public class WeWillMeetAgain extends AbstractLightcone {

    public WeWillMeetAgain(AbstractCharacter<?> owner) {
        super(847, 529, 331, owner);
    }

    @Subscribe
    public void beforeAttack(PreAllyAttack e) {
        e.getAttack().hit(Randf.rand(e.getAttack().getTargets(), getBattle().getGetRandomEnemyRng()), 0.96f);
    }
}
