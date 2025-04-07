package art.ameliah.hsr.relics.ornament;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class FleetOfTheAgeless extends AbstractRelicSetBonus {
    public FleetOfTheAgeless(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public FleetOfTheAgeless(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.HP_PERCENT, 12, "Fleet of the Ageless HP bonus"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        // Doesn't give to memosprites
        getBattle().registerForPlayers(p -> p.addPower(new FleetOfTheAgelessPower()));
    }

    public class FleetOfTheAgelessPower extends PermPower {
        public FleetOfTheAgelessPower() {
            super("Fleet of the Ageless ATK bonus");

            this.setConditionalStat(PowerStat.ATK_PERCENT, this::atkBonus);
        }

        public float atkBonus(AbstractCharacter<?> character) {
            if (FleetOfTheAgeless.this.owner.getFinalSpeed() >= 120) {
                return 8;
            }

            return 0;
        }
    }
}
