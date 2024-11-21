package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.ArrayList;
import java.util.Set;

public class BeforeTheTutorialMissionStarts extends AbstractLightcone {

    public BeforeTheTutorialMissionStarts(AbstractCharacter<?> owner) {
        super(953, 476, 331, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.EFFECT_HIT, 40, "Before The Tutorial Mission Starts Boost"));
    }

    @Override
    public void onAttack(AbstractCharacter<?> character, Set<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
        if (enemiesHit.stream()
                .anyMatch(e -> e.powerList
                        .stream()
                        .anyMatch(p -> p.getStat(PowerStat.DEFENSE_REDUCTION) != 0))) {
            this.owner.increaseEnergy(8, AbstractCharacter.LIGHTCONE_ENERGY_GAIN);
        }
    }

}
