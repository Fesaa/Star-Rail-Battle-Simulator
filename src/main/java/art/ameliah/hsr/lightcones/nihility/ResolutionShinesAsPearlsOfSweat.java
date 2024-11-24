package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class ResolutionShinesAsPearlsOfSweat extends AbstractLightcone {

    public ResolutionShinesAsPearlsOfSweat(AbstractCharacter<?> owner) {
        super(953, 476, 331, owner);
    }

    @Override
    public void beforeAttack(Attack attack) {
        for (AbstractEnemy enemy : attack.getTargets()) {
            if (!enemy.hasPower(Ensnared.NAME)) {
                enemy.addPower(new Ensnared());
            }
        }
    }

    public static class Ensnared extends TempPower {

        public static final String NAME = "Ensnared";


        public Ensnared() {
            this.setName(NAME);
            this.type = PowerType.DEBUFF;
            this.turnDuration = 1;
            this.setStat(PowerStat.DEFENSE_REDUCTION, 16);
        }
    }
}
