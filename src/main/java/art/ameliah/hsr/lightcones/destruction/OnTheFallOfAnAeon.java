package art.ameliah.hsr.lightcones.destruction;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreAllyAttack;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

/**
 * Ignored DMG boost from break
 */
public class OnTheFallOfAnAeon extends AbstractLightcone {

    public OnTheFallOfAnAeon(AbstractCharacter<?> owner) {
        super(1058, 529, 397, owner);
    }

    @Subscribe
    public void beforeAttack(PreAllyAttack e) {
        this.owner.addPower(new OnTheFallOfAnAeonATKBoost());
    }

    public static class OnTheFallOfAnAeonATKBoost extends PermPower {
        public OnTheFallOfAnAeonATKBoost() {
            this.setName(this.getClass().getSimpleName());
            this.maxStacks = 4;
            this.setStat(PowerStat.ATK_PERCENT, 16);
        }


    }
}
