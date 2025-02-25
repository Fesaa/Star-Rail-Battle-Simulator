package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostUltimate;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.List;

public class ScholarLostInErudition extends AbstractRelicSetBonus {

    public ScholarLostInErudition(AbstractCharacter<?> owner) {
        super(owner);
    }

    public ScholarLostInErudition(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        if (!this.isFullSet) {
            return;
        }

        this.owner.addPower(new ScholarLostInEruditionPower());
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_CHANCE, 8, "ScholarLostInErudition CR"));
    }

    public static class ScholarLostInEruditionPower extends PermPower {
        public static final String NAME = "ScholarLostInErudition";

        private boolean extraBoost = false;

        public ScholarLostInEruditionPower() {
            super(NAME);
        }

        @Subscribe
        public void afterUseUltimate(PostUltimate event) {
            this.extraBoost = true;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (damageTypes.contains(DamageType.ULTIMATE)) {
                return 20;
            }

            if (damageTypes.contains(DamageType.SKILL)) {
                float boost = 20 + (this.extraBoost ? 25 : 0);
                this.extraBoost = false;
                return boost;
            }

            return 0;
        }
    }
}
