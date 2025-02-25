package art.ameliah.hsr.game.pf.technicalityentrapment;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.battleLogic.wave.pf.ConcordantTrucePower;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostAllyAttack;

public class ConcordantTruce extends ConcordantTrucePower {

    public ConcordantTruce() {
        super("Concordant Truce");
    }

    @Subscribe
    public void afterAttack(PostAllyAttack event) {
        if (!event.getAttack().getTypes().contains(DamageType.SKILL)) {
            return;
        }

        for (var target : event.getAttack().getTargets()) {
            // https://youtu.be/INVTD86xO_Q?si=4FCiWJ6r7tdO_N1d&t=463 <- got dmg from
            event.getAttack().hitFixed(this, target, 11013);
        }
    }
}
