package art.ameliah.hsr.battleLogic.wave.moc;

import art.ameliah.hsr.battleLogic.AbstractEntity;

// Base class to implement Moc Turbulence, etc from
public abstract class MocTurbulence extends AbstractEntity {

    private boolean firstCycleHasPassed = false;

    public MocTurbulence(String name) {
        this.name = name;
    }

    @Override
    public void takeTurn() {
        this.firstCycleHasPassed = true;
        this.trigger();
    }

    @Override
    public float getBaseAV() {
        return this.firstCycleHasPassed ? 100 : 150;
    }

    public float avUsed() {
        if (this.firstCycleHasPassed) {
            return 150 + (this.numTurnsMetric-1) * 100;
        }
        return 0;
    }

    protected abstract void trigger();
}
