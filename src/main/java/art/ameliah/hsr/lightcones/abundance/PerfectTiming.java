package art.ameliah.hsr.lightcones.abundance;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class PerfectTiming extends AbstractLightcone {

    public PerfectTiming(AbstractCharacter<?> owner) {
        super(953, 423, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.EFFECT_RES, 32, "Perfect Timing Effect Resistance Boost"));
    }

    @Override
    public void onCombatStart() {
        float boost = Math.min(27, (float) (this.owner.getTotalEffectRes() * 0.45));
        this.owner.addPower(PermPower.create(PowerStat.OUTGOING_HEALING, boost, "Perfect Timing Healing Boost"));
    }
}
