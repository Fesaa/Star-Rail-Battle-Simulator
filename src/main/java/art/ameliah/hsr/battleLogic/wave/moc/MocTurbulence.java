package art.ameliah.hsr.battleLogic.wave.moc;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.AlwaysBasicGoal;

// Base class to implement Moc Turbulence, etc from
public abstract class MocTurbulence extends AbstractCharacter<MocTurbulence> {

    private boolean firstCycleHasPassed = false;

    public MocTurbulence() {
        super("MocTurbulence", 0, 0, 0, 0, 0, ElementType.ICE, 0, 0, Path.ABUNDANCE);

        this.registerGoal(0, new AlwaysBasicGoal<>(this));
    }

    @Override
    protected void useSkill() {
    }

    @Override
    protected void useBasic() {
        this.firstCycleHasPassed = true;
        this.trigger();
    }

    @Override
    protected void useUltimate() {
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

    @Override
    public boolean canAdvance() {
        return false;
    }

    protected abstract void trigger();
}
