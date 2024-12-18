package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class BeforeTheTutorialMissionStarts extends AbstractLightcone {

    public BeforeTheTutorialMissionStarts(AbstractCharacter<?> owner) {
        super(953, 476, 331, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.EFFECT_HIT, 40, "Before The Tutorial Mission Starts Boost"));
    }

    @Override
    public void beforeAttack(AttackLogic attack) {
        if (attack.getTargets().stream()
                .anyMatch(e -> e.powerList
                        .stream()
                        .anyMatch(p -> p.getStat(PowerStat.DEFENSE_REDUCTION) != 0))) {
            this.owner.increaseEnergy(8, AbstractCharacter.LIGHTCONE_ENERGY_GAIN);
        }
    }

}
