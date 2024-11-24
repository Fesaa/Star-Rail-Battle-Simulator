package art.ameliah.hsr.battleLogic;

import art.ameliah.hsr.battleLogic.log.lines.entity.GainPower;
import art.ameliah.hsr.battleLogic.log.lines.entity.LosePower;
import art.ameliah.hsr.battleLogic.log.lines.entity.RefreshPower;
import art.ameliah.hsr.battleLogic.log.lines.entity.StackPower;
import art.ameliah.hsr.powers.AbstractPower;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public abstract class AbstractEntity implements BattleEvents,BattleParticipant {
    public String name;
    public float baseSpeed;
    public ArrayList<AbstractPower> powerList = new ArrayList<>();
    public static final int SPEED_PRIORITY_DEFAULT = 99;
    public int speedPriority = SPEED_PRIORITY_DEFAULT;
    public int numTurnsMetric = 0;
    @Setter
    private IBattle battle;

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

    private final Collection<BattleEvents> listeners = new ConcurrentLinkedQueue<>();

    protected Collection<BattleEvents> getListeners() {
        return listeners;
    }

    public void addListener(BattleEvents listener) {
        listeners.add(listener);
    }

    public void emit(Consumer<BattleEvents> event) {
        synchronized (this.listeners) {
            this.listeners.forEach(event);

            // Character itself should be last to receive the event, as buffs have to be applied first.
            event.accept(this);
        }
    }

    public float getBaseAV() {
        return (float)10000 / baseSpeed;
    }

    public void takeTurn() {
        resetSpeedPriority(); //reset speed priority if it was changed
    }

    public void addPower(AbstractPower power) {
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
        this.listeners.add(power);
    }

    public void removePower(AbstractPower power) {
        power.onRemove();
        powerList.remove(power);
        if (!getBattle().getLessMetrics()) {
            getBattle().addToLog(new LosePower(this, power));
        }
        this.listeners.remove(power);
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

    public String toString() {
        return name;
    }

    public void resetSpeedPriority() {
        speedPriority = SPEED_PRIORITY_DEFAULT;
    }

    public float getCurrentHp() {
        return 0;
    }
}
