package art.ameliah.hsr.lightcones.hunt;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.List;

public class OnlySilenceRemains extends AbstractLightcone {

    public OnlySilenceRemains(AbstractCharacter<?> owner) {
        super(953, 476, 331, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.ATK_PERCENT, 32, "Only Silence Remains Attack Boost"));
        this.owner.addPower(new OnlySilenceRemainsPower());
    }

    public static class OnlySilenceRemainsPower extends PermPower {
        public OnlySilenceRemainsPower() {
            this.setName(this.getClass().getSimpleName());
        }

        @Override
        public float getConditionalCritRate(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (getBattle().getEnemies().size() < 3) {
                return 24;
            }

            return 0;
        }
    }
}
