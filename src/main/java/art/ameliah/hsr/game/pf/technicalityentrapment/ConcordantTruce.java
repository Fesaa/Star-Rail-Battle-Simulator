package art.ameliah.hsr.game.pf.technicalityentrapment;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.wave.pf.ConcordantTrucePower;
import art.ameliah.hsr.characters.DamageType;

public class ConcordantTruce extends ConcordantTrucePower {

    public ConcordantTruce() {
        super("Concordant Truce");
    }

    @Override
    public void onAttack(Attack attack) {
        if (!attack.getTypes().contains(DamageType.SKILL)) {
            return;
        }

        for (var target : attack.getTargets()) {
            // https://youtu.be/INVTD86xO_Q?si=4FCiWJ6r7tdO_N1d&t=463 <- got dmg from
            attack.hitFixed(this, target, 11013);
        }
    }
}
