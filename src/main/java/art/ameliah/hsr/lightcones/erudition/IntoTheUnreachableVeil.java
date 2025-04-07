package art.ameliah.hsr.lightcones.erudition;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostUltimate;
import art.ameliah.hsr.events.character.PreUltimate;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

import java.util.List;

public class IntoTheUnreachableVeil extends AbstractLightcone {
    public IntoTheUnreachableVeil(AbstractCharacter<?> owner) {
        super(952, 635, 463, owner);
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent e) {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_CHANCE, 12, "IntoTheUnreachableVeil CR"));
    }

    @Subscribe
    public void onUseUltimate(PreUltimate e) {
        this.owner.addPower(new IntoTheUnreachableVeilPower());
    }

    @Subscribe
    public void afterUseUltimate(PostUltimate e) {
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
