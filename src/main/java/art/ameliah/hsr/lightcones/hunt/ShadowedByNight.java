package art.ameliah.hsr.lightcones.hunt;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreAllyAttack;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class ShadowedByNight extends AbstractLightcone {

    public ShadowedByNight(AbstractCharacter<?> owner) {
        super(847, 476, 397, owner);
    }


    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.BREAK_EFFECT, 56, "Shadowed By Night Break Effect Boost"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        getBattle().IncreaseSpeed(this.owner, new ShadowedByNightPower());
    }

    @Subscribe
    public void beforeAttack(PreAllyAttack event) {
        if (!event.getAttack().getTypes().contains(DamageType.BREAK)) return;

        getBattle().IncreaseSpeed(this.owner, new ShadowedByNightPower());
    }

    public static class ShadowedByNightPower extends TempPower {
        public ShadowedByNightPower() {
            this.setName(this.getClass().getSimpleName());
            this.turnDuration = 2;
            this.setStat(PowerStat.SPEED_PERCENT, 12);
        }
    }
}
