package art.ameliah.hsr.battleLogic.wave.moc;

import art.ameliah.hsr.battleLogic.AbstractEntity;

// Base class to implement Moc Turbulence, etc from
public abstract class MocTurbulence extends AbstractEntity {

    private boolean firstCycleHasPassed = false;

    @Override
    public float getBaseAV() {
        return this.firstCycleHasPassed ? 100 : 150;
    }

    public float avUsed() {
        if (this.firstCycleHasPassed) {
            return 150 + this.numTurnsMetric * 100;
        }
        return 0;
    }
}
