package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreAllyAttack;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class ResolutionShinesAsPearlsOfSweat extends AbstractLightcone {

    public ResolutionShinesAsPearlsOfSweat(AbstractCharacter<?> owner) {
        super(953, 476, 331, owner);
    }

    @Subscribe
    public void beforeAttack(PreAllyAttack e) {
        for (AbstractEnemy enemy : e.getAttack().getTargets()) {
            if (!enemy.hasPower(Ensnared.NAME)) {
                enemy.addPower(new Ensnared());
            }
        }
    }

    public static class Ensnared extends TempPower {

        public static final String NAME = "Ensnared";


        public Ensnared() {
            this.name = name;
            this.type = PowerType.DEBUFF;
            this.turnDuration = 1;
            this.setStat(PowerStat.DEFENSE_REDUCTION, 16);
        }
    }
}
