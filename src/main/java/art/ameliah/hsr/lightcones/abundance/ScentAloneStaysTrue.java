package art.ameliah.hsr.lightcones.abundance;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

import java.util.ArrayList;

public class ScentAloneStaysTrue extends AbstractLightcone {

    public ScentAloneStaysTrue(AbstractCharacter<?> owner) {
        super(1058, 529, 529, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.BREAK_EFFECT, 60, "Scent Alone Stays True Break Effect Boost"));
    }

    @Override
    public void onAttack(AbstractCharacter<?> character, ArrayList<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
        if (!types.contains(DamageType.ULTIMATE)) return;

        enemiesHit.forEach(e -> {
            float dmg = this.owner.getTotalBreakEffect() >= 150 ? 18 : 10;
            e.addPower(TempPower.create(PowerStat.DAMAGE_TAKEN, dmg, 2, "Scent Alone Stays True Damage Taken Debuff"));
        });
    }
}
