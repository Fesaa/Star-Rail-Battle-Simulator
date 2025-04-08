package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostAllyAttack;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class LiesAflutterInTheWind extends AbstractLightcone {
    public LiesAflutterInTheWind(AbstractCharacter<?> owner) {
        super(953, 582, 529, owner);
    }


    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.SPEED_PERCENT, 18, "Lies, Aflutter in the Wind SPD Boost"));
    }

    @Subscribe
    public void afterAttack(PostAllyAttack event) {
        event.getAttack().getTargets().forEach(e -> {
            if (this.owner.successFullHit(120, e)) {
                e.addPower(TempPower.create(PowerStat.DEFENSE_REDUCTION, 16, 2, "Bamboozle"));
            }

            if (this.owner.getFinalSpeed() < 170) {
                return;
            }

            if (this.owner.successFullHit(120, e)) {
                e.addPower(TempPower.create(PowerStat.DEFENSE_REDUCTION, 8, 2, "Theft"));
            }
        });
    }
}
