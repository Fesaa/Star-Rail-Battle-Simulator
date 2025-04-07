package art.ameliah.hsr.lightcones.destruction;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class UnderTheBlueSky extends AbstractLightcone {

    private final float upTime;

    public UnderTheBlueSky(AbstractCharacter<?> owner) {
        this(owner, 1);
    }

    public UnderTheBlueSky(AbstractCharacter<?> owner, float upTime) {
        super(953, 476, 331, owner);
        this.upTime = Math.max(0, Math.min(1, upTime));
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.ATK_PERCENT, 32, "Under The Blue Sky ATK Boost"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent e) {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_CHANCE, 24 * this.upTime, "Under The Blue Sky Crit Boost"));
    }
}
