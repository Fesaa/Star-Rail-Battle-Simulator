package art.ameliah.hsr.lightcones.harmony;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.metrics.CounterMetric;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class IfTimeWereAFlower extends AbstractLightcone {

    private final CounterMetric<Integer> presage;

    public IfTimeWereAFlower(AbstractCharacter<?> owner) {
        super(1270, 529, 397, owner);

        this.presage = this.owner.getMetricRegistry()
                .register(CounterMetric.newIntegerCounter(this.owner.getName() + "::presage-counter"));
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_DAMAGE, 36, "If Time Were a Flower CD Boost"));
    }

    @Override
    public void onCombatStart() {
        this.owner.increaseEnergy(30, "If Time Were a Flower");
        this.presage.set(60);
        getBattle().registerForPlayers(p -> p.addPower(new IfTimeWereAFlowerListener()));
    }

    @Override
    public void afterUseUltimate() {
        float energy = this.presage.get() * 0.3f;
        float CD = this.presage.get();

        this.owner.increaseEnergy(energy, "If Time Were a Flower Presage consumption");
        getBattle().getPlayers().forEach(player -> {
            player.addPower(TempPower.create(PowerStat.CRIT_DAMAGE, CD, 3, "If Time Were a Flower Presage consumption CD Boost"));
        });

        this.presage.set(0);
    }

    public class IfTimeWereAFlowerListener extends PermPower {

        @Override
        public void afterAttack(AttackLogic attack) {
            IfTimeWereAFlower.this.presage.increase(attack.getTargets().size(), 60);
        }
    }
}
