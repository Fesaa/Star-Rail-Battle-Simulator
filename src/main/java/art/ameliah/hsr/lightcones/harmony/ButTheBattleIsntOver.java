package art.ameliah.hsr.lightcones.harmony;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreUltimate;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

/**
 * This lightcone assumes onSpecificTrigger is called when the character uses their skill, and that
 * the passed character is next to play
 */
public class ButTheBattleIsntOver extends AbstractLightcone {

    public ButTheBattleIsntOver(AbstractCharacter<?> owner) {
        super(1164, 529, 463, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.ENERGY_REGEN, 10, "But The Battle Isn't Over Energy Regen Boost"));
    }

    public void onSpecificTrigger(AbstractCharacter<?> character, AbstractEnemy enemy) {
        if (character == null) return;

        character.addPower(TempPower.create(PowerStat.DAMAGE_BONUS, 30, 1, "But The Battle Isn't Over Damage Boost"));
    }

    @Subscribe
    public void onUseUltimate(PreUltimate e) {
        // Metric is incremented before hook is called, so there is an offset.
        if (this.owner.getActionMetric().frequency(MoveType.ULTIMATE) % 2 == 1) {
            getBattle().generateSkillPoint(this.owner, 1);
        }
    }
}
