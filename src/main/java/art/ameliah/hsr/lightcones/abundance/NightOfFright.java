package art.ameliah.hsr.lightcones.abundance;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreUltimate;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

/**
 * There is no hook for healing, so you may pass the amount of stacks to the constructor.
 * If you're using HuoHuo, don't pass any and assume 5 stacks at all times.
 */
public class NightOfFright extends AbstractLightcone {

    private final int atkStacks;

    public NightOfFright(AbstractCharacter<?> owner) {
        this(owner, 5);
    }

    public NightOfFright(AbstractCharacter<?> owner, int atkStacks) {
        super(1164, 476, 529, owner);
        this.atkStacks = Math.min(5, atkStacks);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.ENERGY_REGEN, 12, "Night of Fright Energy Regen Boost"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        getBattle().registerForPlayers(c -> {
            c.addPower(new NightOfFrightPower());
            c.addPower(new NightOfFrightListener());
        });
    }

    public class NightOfFrightPower extends PermPower {
        public NightOfFrightPower() {
            this.setName(this.getClass().getSimpleName());

            this.setConditionalStat(PowerStat.ATK_PERCENT, _ -> 2.4f * atkStacks);
        }
    }

    public class NightOfFrightListener extends PermPower {

        @Subscribe
        public void onUlt(PreUltimate e) {
            var owner = ((AbstractCharacter<?>) this.owner);
            owner.increaseHealth(NightOfFright.this, owner.getFinalHP() * 0.1f);
        }
    }
}
