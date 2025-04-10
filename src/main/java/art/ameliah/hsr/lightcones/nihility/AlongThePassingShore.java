package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreAllyAttack;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.List;

public class AlongThePassingShore extends AbstractLightcone {

    public AlongThePassingShore(AbstractCharacter<?> owner) {
        super(1058, 635, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_DAMAGE, 36, "Along The Passing Shore Crit Damage Boost"));
    }

    @Subscribe
    public void beforeAttack(PreAllyAttack e) {
        for (AbstractEnemy enemy : e.getAttack().getTargets()) {
            enemy.addPower(new MirageFizzle(this));
        }
    }

    public static class MirageFizzle extends PermPower {

        private final AlongThePassingShore lightcone;

        public MirageFizzle(AlongThePassingShore lightcone) {
            this.setName(this.getClass().getSimpleName());
            this.lightcone = lightcone;
            this.type = PowerType.DEBUFF;
        }

        @Override
        public float receiveConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (character != this.lightcone.owner) return 0;

            if (damageTypes.contains(DamageType.ULTIMATE)) {
                return 0.24f * 2;
            }

            return 0.24f;
        }
    }
}
