package art.ameliah.hsr.lightcones.erudition;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

import java.util.List;

public class IntoTheUnreachableVeil extends AbstractLightcone {
    public IntoTheUnreachableVeil(AbstractCharacter<?> owner) {
        super(952, 635, 463, owner);
    }

    @Override
    public void onCombatStart() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_CHANCE, 12, "IntoTheUnreachableVeil CR"));
    }

    @Override
    public void onUseUltimate() {
        this.owner.addPower(new IntoTheUnreachableVeilPower());
    }

    @Override
    public void afterUseUltimate() {
        if (this.owner.maxEnergy < 140) {
            return;
        }

        getBattle().generateSkillPoint(this.owner, 1);
    }

    public static class IntoTheUnreachableVeilPower extends TempPower {

        public static final String NAME = "Into the Unreachable Veil power";

        public IntoTheUnreachableVeilPower() {
            super(3, NAME);
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (damageTypes.contains(DamageType.ULTIMATE) || damageTypes.contains(DamageType.SKILL)) {
                return 48;
            }
            return 0;
        }
    }
}
