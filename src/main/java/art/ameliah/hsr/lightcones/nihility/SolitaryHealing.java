package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreUltimate;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

import java.util.List;

/**
 * Energy regen not implemented, no hooks
 */
public class SolitaryHealing extends AbstractLightcone {

    public SolitaryHealing(AbstractCharacter<?> owner) {
        super(1058, 529, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.BREAK_EFFECT, 40, "Solitary Healing Break Boost"));
    }

    @Subscribe
    public void onUseUltimate(PreUltimate e) {
        this.owner.addPower(new SolitaryHealingBoost());
    }

    public static class SolitaryHealingBoost extends TempPower {
        public SolitaryHealingBoost() {
            this.setName(this.getClass().getSimpleName());
            this.turnDuration = 2;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (!damageTypes.contains(DamageType.DOT)) return 0;

            return 48;
        }
    }


}
