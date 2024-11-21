package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

import java.util.ArrayList;
import java.util.Set;

public class ThoseManySprings extends AbstractLightcone {

    public ThoseManySprings(AbstractCharacter<?> owner) {
        super(953, 582, 529, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.EFFECT_HIT, 60, "Those Many Springs Effect Hit Boost"));
    }

    @Override
    public void onAttack(AbstractCharacter<?> character, Set<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
        if (!types.contains(DamageType.BASIC)
                && !types.contains(DamageType.SKILL)
                && !types.contains(DamageType.ULTIMATE)) {
            return;
        }

        for (AbstractEnemy enemy : enemiesHit) {
            if (enemy.hasPower(Unarmored.NAME)) {
                enemy.removePower(Unarmored.NAME);
                enemy.addPower(new Cornered());
            } else if (!enemy.hasPower(Cornered.NAME)) {
                enemy.addPower(new Unarmored());
            }
        }
    }

    public static class Unarmored extends TempPower {
        public static final String NAME = "Unarmored";

        public Unarmored() {
            super(2, NAME);
            this.setStat(PowerStat.DAMAGE_TAKEN, 10);
            this.type = PowerType.DEBUFF;
        }
    }

    public static class Cornered extends TempPower {
        public static final String NAME = "Cornered";

        public Cornered() {
            super(2, NAME);
            this.setStat(PowerStat.DAMAGE_TAKEN, 24);
            this.type = PowerType.DEBUFF;
        }
    }
}
