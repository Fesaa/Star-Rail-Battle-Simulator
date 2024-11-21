package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

import java.util.ArrayList;
import java.util.Set;

public class ResolutionShinesAsPearlsOfSweat extends AbstractLightcone {

    public ResolutionShinesAsPearlsOfSweat(AbstractCharacter<?> owner) {
        super(953, 476, 331, owner);
    }

    @Override
    public void onAttack(AbstractCharacter<?> character, Set<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
        for (AbstractEnemy enemy : enemiesHit) {
            if (!enemy.hasPower(Ensnared.NAME)) {
                enemy.addPower(new Ensnared());
            }
        }
    }

    public static class Ensnared extends TempPower {

        public static String NAME = "Ensnared";


        public Ensnared() {
            this.name = NAME;
            this.type = PowerType.DEBUFF;
            this.turnDuration = 1;
            this.setStat(PowerStat.DEFENSE_REDUCTION, 16);
        }
    }
}
