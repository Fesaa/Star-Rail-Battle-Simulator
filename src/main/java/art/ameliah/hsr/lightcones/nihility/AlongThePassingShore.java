package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.ArrayList;

public class AlongThePassingShore extends AbstractLightcone {

    public AlongThePassingShore(AbstractCharacter<?> owner) {
        super(1058, 635, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_DAMAGE, 36, "Along The Passing Shore Crit Damage Boost"));
    }

    @Override
    public void onAttack(AbstractCharacter<?> character, ArrayList<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
        for (AbstractEnemy enemy : enemiesHit) {
            enemy.addPower(new MirageFizzle(this));
        }
    }

    public static class MirageFizzle extends PermPower {

        private final AlongThePassingShore lightcone;

        public MirageFizzle(AlongThePassingShore lightcone) {
            this.name = this.getClass().getSimpleName();
            this.lightcone = lightcone;
            this.type = PowerType.DEBUFF;
        }

        @Override
        public float receiveConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            if (character != this.lightcone.owner) return 0;

            if (damageTypes.contains(DamageType.ULTIMATE)) {
                return 0.24f * 2;
            }

            return 0.24f;
        }
    }
}
