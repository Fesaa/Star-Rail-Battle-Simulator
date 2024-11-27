package art.ameliah.hsr.lightcones.abundance;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class ScentAloneStaysTrue extends AbstractLightcone {

    public ScentAloneStaysTrue(AbstractCharacter<?> owner) {
        super(1058, 529, 529, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.BREAK_EFFECT, 60, "Scent Alone Stays True Break Effect Boost"));
    }

    @Override
    public void beforeAttack(AttackLogic attack) {
        if (!attack.getTypes().contains(DamageType.ULTIMATE)) return;

        attack.getTargets().forEach(e -> {
            float dmg = this.owner.getTotalBreakEffect() >= 150 ? 18 : 10;
            e.addPower(TempPower.create(PowerStat.DAMAGE_TAKEN, dmg, 2, "Scent Alone Stays True Damage Taken Debuff"));
        });
    }
}
