package art.ameliah.hsr.lightcones.preservation;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import java.util.ArrayList;
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

    @Override
    public void onAttack(AbstractCharacter<?> character, ArrayList<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
        double dmg = character.getFinalDefense() * 0.8;
        // TODO: Add dot to enemies
        // for (AbstractEnemy enemy : enemiesHit) {
        //
        // }
    }
}
