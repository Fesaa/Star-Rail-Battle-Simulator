package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.utils.Randf;

/**
 * Missing CR boost
 */
public class IncessantRain extends AbstractLightcone {

    public IncessantRain(AbstractCharacter<?> owner) {
        super(1058, 582, 463, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.EFFECT_HIT, 24, "Incessant Rain Effect Hit Boost"));
    }

    @Override
    public void beforeAttack(Attack attack) {
        if (!attack.getTypes().contains(DamageType.BASIC)
                && !attack.getTypes().contains(DamageType.SKILL)
                && !attack.getTypes().contains(DamageType.ULTIMATE)) {
            return;
        }

        if (attack.getTargets().isEmpty()) {
            return;
        }

        AbstractEnemy target = Randf.rand(attack.getTargets(), getBattle().getAetherRng());
        target.addPower(new AetherCode());
    }

    public static class AetherCode extends TempPower {
        public AetherCode() {
            this.setName(this.getClass().getSimpleName());
            this.turnDuration = 1;
            this.setStat(PowerStat.DAMAGE_TAKEN, 12);
        }
    }
}
