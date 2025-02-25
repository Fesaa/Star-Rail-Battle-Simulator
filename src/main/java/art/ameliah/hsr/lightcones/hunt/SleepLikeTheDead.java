package art.ameliah.hsr.lightcones.hunt;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreAllyAttack;
import art.ameliah.hsr.events.combat.TurnStartEvent;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

/**
 * No info on crit after hit, so always missing crit after cooldown is over
 */
public class SleepLikeTheDead extends AbstractLightcone {

    private int cooldown = 0;

    public SleepLikeTheDead(AbstractCharacter<?> owner) {
        super(1058, 582, 463, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_DAMAGE, 36, "Sleep Like The Dead Crit Damage Boost"));
    }

    @Subscribe
    public void beforeAttack(PreAllyAttack e) {
        if (this.cooldown <= 0 && (e.getAttack().getTypes().contains(DamageType.SKILL) || e.getAttack().getTypes().contains(DamageType.BASIC))) {
            AbstractPower critPower = TempPower.create(PowerStat.CRIT_CHANCE, 36, 1, "Sleep Like The Dead Crit Chance Boost");
            critPower.justApplied = true;
            this.owner.addPower(critPower);
            cooldown = 3;
        }
    }

    @Subscribe
    public void onTurnStart(TurnStartEvent e) {
        if (cooldown > 0) {
            cooldown--;
        }
    }
}
