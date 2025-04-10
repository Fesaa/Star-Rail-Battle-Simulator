package art.ameliah.hsr.relics.ornament;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.List;

public class FirmamentFrontlineGlamoth extends AbstractRelicSetBonus {
    public FirmamentFrontlineGlamoth(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public FirmamentFrontlineGlamoth(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.ATK_PERCENT, 12, "Firmament Frontline Glamoth ATK bonus"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        this.owner.addPower(new FirmamentFrontlineGlamothDMGBonus());
    }

    public class FirmamentFrontlineGlamothDMGBonus extends PermPower {
        public FirmamentFrontlineGlamothDMGBonus() {
            super("Firmament Frontline Glamoth DMG bonus");
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (FirmamentFrontlineGlamoth.this.owner.getFinalSpeed() > 160) {
                return 18;
            }
            if (FirmamentFrontlineGlamoth.this.owner.getFinalSpeed() > 135) {
                return 12;
            }
            return 0;
        }
    }
}
