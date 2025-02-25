package art.ameliah.hsr.lightcones.preservation;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreAllyAttack;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class TrendOfTheUniversalMarket extends AbstractLightcone {

    public TrendOfTheUniversalMarket(AbstractCharacter<?> owner) {
        super(1058, 370, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.DEF_PERCENT, 32, "Trend Of The Universal Market Defense Boost"));
    }

    @Subscribe
    public void beforeAttack(PreAllyAttack e) {
        double dmg = e.getAttack().getSource().getFinalDefense() * 0.8;
        // TODO: Add dot to enemies
        // for (AbstractEnemy enemy : enemiesHit) {
        //
        // }
    }
}
