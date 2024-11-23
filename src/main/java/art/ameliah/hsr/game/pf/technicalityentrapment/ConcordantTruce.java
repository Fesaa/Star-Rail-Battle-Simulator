package art.ameliah.hsr.game.pf.technicalityentrapment;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.wave.pf.ConcordantTrucePower;
import art.ameliah.hsr.characters.DamageType;

public class ConcordantTruce extends ConcordantTrucePower {

    public ConcordantTruce() {
        super("Concordant Truce");
    }

    @Override
    public void afterAttackFinish(Attack attack) {
        if (!attack.getTypes().contains(DamageType.SKILL)) {
            return;
        }

        for (var target : attack.getTargets()) {
            // TODO: Figure out how much, no clue
            // target.dealDmg();
        }
    }
}
