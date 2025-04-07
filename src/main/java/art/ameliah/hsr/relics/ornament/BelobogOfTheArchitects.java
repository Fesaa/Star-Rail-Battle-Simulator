package art.ameliah.hsr.relics.ornament;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class BelobogOfTheArchitects extends AbstractRelicSetBonus {
    public BelobogOfTheArchitects(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public BelobogOfTheArchitects(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.DEF_PERCENT, 15, "Belobog of the Architects DEF bonus"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        this.owner.addPower(new BelobogOfTheArchitectsPower());
    }

    public static class BelobogOfTheArchitectsPower extends PermPower {
        public BelobogOfTheArchitectsPower() {
            super("Belobog of the Architects extra DEF bonus");

            this.setConditionalStat(PowerStat.DEF_PERCENT, this::defenseBonus);
        }

        public float defenseBonus(AbstractCharacter<?> character) {
            if (character.getTotalEHR() >= 50) {
                return 15;
            }

            return 0;
        }
    }
}
