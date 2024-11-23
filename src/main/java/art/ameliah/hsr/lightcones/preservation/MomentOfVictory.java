package art.ameliah.hsr.lightcones.preservation;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

import java.util.List;

public class MomentOfVictory extends AbstractLightcone {

    public MomentOfVictory(AbstractCharacter<?> owner) {
        super(1058, 476, 595, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.DEF_PERCENT, 24, "Moment Of Victory Defense Boost"));
        this.owner.addPower(PermPower.create(PowerStat.EFFECT_HIT, 24, "Moment Of Victory Effect Hit Boost"));
        this.owner.addPower(PermPower.create(PowerStat.TAUNT_VALUE, 200, "Moment Of Victory Taunt Value Boost"));
    }

    @Override
    public void afterAttacked(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> types, int energyFromAttacked, float totalDmg) {
        this.owner.addPower(TempPower.create(PowerStat.DEF_PERCENT, 24, 1, "Moment Of Victory Defense Boost"));
    }
}
