package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.battleLogic.BattleHelpers;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;

import java.util.ArrayList;

public class WeWillMeetAgain extends AbstractLightcone {

    public WeWillMeetAgain(AbstractCharacter<?> owner) {
        super(847, 529, 331, owner);
    }

    @Override
    public void onAttack(AbstractCharacter<?> character, ArrayList<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
        for (AbstractEnemy enemy : enemiesHit) {
            getBattle().getHelper().additionalDamageHitEnemy(this.owner, enemy, 96, BattleHelpers.MultiplierStat.ATK);
        }
    }
}
