package art.ameliah.hsr.lightcones.harmony;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreUltimate;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class PastSelfInMirror extends AbstractLightcone {

    public PastSelfInMirror(AbstractCharacter<?> owner) {
        super(1058, 529, 529, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.BREAK_EFFECT, 60, "Past Self in Mirror Break Effect Boost"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        getBattle().getPlayers().stream()
                .filter(c -> c.usesEnergy)
                .forEach(c -> c.increaseEnergy(10, false, AbstractCharacter.LIGHTCONE_ENERGY_GAIN));
    }

    @Subscribe
    public void onUseUltimate(PreUltimate e) {
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.addPower(TempPower.create(PowerStat.DAMAGE_BONUS, 24, 3, "Past Self in Mirror Damage Boost"));
        }
        if (this.owner.getTotalBreakEffect() > 150) {
            getBattle().generateSkillPoint(this.owner, 1);
        }
    }
}
