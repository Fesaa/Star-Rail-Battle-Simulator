package art.ameliah.hsr.lightcones.harmony;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import java.util.ArrayList;
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

    @Override
    public void onCombatStart() {
        AbstractPower power = new FlowingNightglowPower();
        getBattle().getPlayers().forEach(c -> c.addPower(power));
    }

    @Override
    public void onEndTurn() {
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            if (character.hasPower(cadenzaBuff.name)) {
                character.removePower(cadenzaBuff);
            }
        }
    }

    @Override
    public void onUseUltimate() {
        owner.removePower(ERPowerName);
        this.owner.addPower(TempPower.create(PowerStat.ATK_PERCENT, 48, 1, "Flowing Nightglow ATK Boost"));
        getBattle().getPlayers().forEach(c -> c.addPower(cadenzaBuff));
    }

    public class FlowingNightglowPower extends PermPower {
        public FlowingNightglowPower() {
            this.name = this.getClass().getSimpleName();
        }
        @Override
        public void onBeforeUseAttack(ArrayList<DamageType> types) {
            FlowingNightglow.this.owner.addPower(new FlowingNightglowERRPower());
        }
    }

    public static class FlowingNightglowERRPower extends PermPower {
        public FlowingNightglowERRPower() {
            this.name = ERPowerName;
            this.maxStacks = 5;
        }
        @Override
        public float getConditionalERR(AbstractCharacter<?> character) {
            return 3 * stacks;
        }
    }

}