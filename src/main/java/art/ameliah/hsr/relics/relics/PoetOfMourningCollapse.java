package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostSummon;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class PoetOfMourningCollapse extends AbstractRelicSetBonus {

    private int crBoost = 0;

    public PoetOfMourningCollapse(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public PoetOfMourningCollapse(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.QUANTUM_DMG_BOOST, 10, "Poet of Mourning Collapse Quantum Boost"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        this.owner.addPower(PermPower.create(PowerStat.SPEED_PERCENT, -8, "Poet of Mourning Collapse Speed Reduction"));
        getBattle().getActionValueMap().put(this.owner, this.owner.getBaseAV());

        if (this.owner.getFinalSpeed() < 95) {
            this.crBoost = 32;
        } else if (this.owner.getFinalSpeed() < 110) {
            this.crBoost = 20;
        }

        if (this.crBoost > 0) {
            this.owner.addPower(PermPower.create(PowerStat.CRIT_CHANCE, 32, "Poet of Mourning Collapse Critical Chance"));
        }
    }

    @Subscribe
    public void onSummon(PostSummon e) {
        if (this.crBoost > 0) {
            e.getMemosprite().addPower(PermPower.create(PowerStat.CRIT_CHANCE, 32, "Poet of Mourning Collapse Critical Chance"));
        }
    }
}
