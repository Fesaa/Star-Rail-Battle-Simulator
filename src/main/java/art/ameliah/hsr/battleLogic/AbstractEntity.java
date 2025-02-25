package art.ameliah.hsr.battleLogic;

import art.ameliah.hsr.battleLogic.log.lines.entity.GainPower;
import art.ameliah.hsr.battleLogic.log.lines.entity.LosePower;
import art.ameliah.hsr.events.EventBus;
import art.ameliah.hsr.events.EventPriority;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.metrics.CounterMetric;
import art.ameliah.hsr.metrics.MetricRegistry;
import art.ameliah.hsr.powers.AbstractPower;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Subscribe(priority = EventPriority.HIGHEST)
public abstract class AbstractEntity implements BattleParticipant {

    public static final int SPEED_PRIORITY_DEFAULT = 99;
    @Getter
    protected final EventBus eventBus = new EventBus();

    @Getter
    protected MetricRegistry metricRegistry = new MetricRegistry(this);
    protected CounterMetric<Integer> turnsMetric = metricRegistry.register(CounterMetric.newIntegerCounter("Turns taken", "Amount of taken turns"));
    @Getter
    protected CounterMetric<Float> currentHp = metricRegistry.register(CounterMetric.newFloatCounter("enemy-hp", "Left over HP"));

    @Getter
    private Collection<AbstractPower> preCombatPowers;

    public List<AbstractPower> powerList = new ArrayList<>();
    public float baseSpeed;
    public int speedPriority = SPEED_PRIORITY_DEFAULT;

    protected String name;
    @Setter
    private IBattle battle;

    public AbstractEntity() {
        this.eventBus.registerListener(this);
    }

    public void SetPreCombatPowers() {
        if (this.preCombatPowers != null) {
            throw new IllegalStateException("PreCombat Powers can only be set once");
        }
        this.preCombatPowers = new ArrayList<>(this.powerList);
    }

    @Override
    public IBattle getBattle() {
        return this.battle;
    }

    public String getName() {
        if (this.name == null) {
            return this.getClass().getSimpleName();
        }
        return this.name;
    }

    public void addListener(Object listener) {
        this.eventBus.registerListener(listener);
    }

    public float getBaseAV() {
        return (float) 10000 / baseSpeed;
    }

    public void takeTurn() {
        resetSpeedPriority(); //reset speed priority if it was changed
    }

    public void addPower(@NotNull AbstractPower power) {
        for (AbstractPower existingPower : this.powerList) {
            if (existingPower.getName().equals(power.getName())) {
                existingPower.merge(power);
                return;
            }
        }
        powerList.add(power);
        power.setOwner(this);
        if (inBattle()) {
            if (!getBattle().getLessMetrics()) {
                getBattle().addToLog(new GainPower(this, power));
            }
        }
        this.eventBus.registerListener(power);
    }

    public void removePower(AbstractPower power) {
        power.onRemove();
        powerList.remove(power);
        if (!getBattle().getLessMetrics()) {
            getBattle().addToLog(new LosePower(this, power));
        }
        this.eventBus.unregisterListener(power);
    }

    public void removePower(String name) {
        for (AbstractPower power : powerList) {
            if (power.getName().equals(name)) {
                removePower(power);
                return;
            }
        }
    }

    public boolean hasPower(String powerName) {
        for (AbstractPower power : powerList) {
            if (power.getName().equals(powerName)) {
                return true;
            }
        }
        return false;
    }

    public AbstractPower getPower(String powerName) {
        for (AbstractPower power : powerList) {
            if (power.getName().equals(powerName)) {
                return power;
            }
        }
        return null;
    }

    public int getPowerStacks(String powerName) {
        var power = this.getPower(powerName);
        return power == null ? 0 : power.stacks;
    }

    public String toString() {
        return getName();
    }

    public void resetSpeedPriority() {
        speedPriority = SPEED_PRIORITY_DEFAULT;
    }

    public int getTurns() {
        return this.turnsMetric.get();
    }
}
