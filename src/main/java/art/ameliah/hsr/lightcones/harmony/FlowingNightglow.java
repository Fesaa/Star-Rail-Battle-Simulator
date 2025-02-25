package art.ameliah.hsr.lightcones.harmony;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreAllyAttack;
import art.ameliah.hsr.events.character.PreUltimate;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.events.combat.TurnEndEvent;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;


public class FlowingNightglow extends AbstractLightcone {
    public static final String ERPowerName = "FlowingNightglowERRPower";

    // As long as robin has cadenza, everyone has cadenza, so we make this a perm power that we manually add and remove
    private final AbstractPower cadenzaBuff = PermPower.create(PowerStat.DAMAGE_BONUS, 24, "Flowing Nightglow DMG Boost");

    public FlowingNightglow(AbstractCharacter<?> owner) {
        super(953, 635, 463, owner);
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        AbstractPower power = new FlowingNightglowPower();
        getBattle().registerForPlayers(c -> c.addPower(power));
    }

    @Subscribe
    public void onEndTurn(TurnEndEvent e) {
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            if (character.hasPower(cadenzaBuff.getName())) {
                character.removePower(cadenzaBuff);
            }
        }
    }

    @Subscribe
    public void onUseUltimate(PreUltimate event) {
        owner.removePower(ERPowerName);
        this.owner.addPower(TempPower.create(PowerStat.ATK_PERCENT, 48, 1, "Flowing Nightglow ATK Boost"));
        getBattle().getPlayers().forEach(c -> c.addPower(cadenzaBuff));
    }

    public static class FlowingNightglowERRPower extends PermPower {
        public FlowingNightglowERRPower() {
            this.setName(ERPowerName);
            this.maxStacks = 5;

            this.setConditionalStat(PowerStat.ENERGY_REGEN, _ -> 3f * this.stacks);
        }
    }

    public class FlowingNightglowPower extends PermPower {
        public FlowingNightglowPower() {
            this.setName(this.getClass().getSimpleName());
        }

        @Subscribe
        public void beforeAttack(PreAllyAttack e) {
            FlowingNightglow.this.owner.addPower(new FlowingNightglowERRPower());
        }
    }

}
