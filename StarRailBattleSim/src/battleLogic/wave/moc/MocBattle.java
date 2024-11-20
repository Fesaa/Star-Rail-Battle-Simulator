package battleLogic.wave.moc;

import battleLogic.wave.Wave;
import battleLogic.wave.WavedBattle;
import characters.AbstractCharacter;

public class MocBattle extends WavedBattle {

    private final MocTurbulence turbulence;

    public MocBattle(MocTurbulence turbulence, MocWave phaseOne, MocWave phaseTwo) {
        this.turbulence = turbulence;
        this.addWave(phaseOne);
        this.addWave(phaseTwo);
    }

    public MocBattle(MocTurbulence turbulence) {
        this.turbulence = turbulence;
    }

    @Override
    public void addWave(Wave wave) {
        if (this.waves.size() == 2) {
            throw new IllegalStateException("MocBattle already has two waves");
        }
        super.addWave(wave);
    }

    @Override
    protected void onWaveChange() {
        this.battleLength = this.initialBattleLength - this.turbulence.avUsed();

        getPlayers().forEach(p -> {
            this.actionValueMap.put(p, p.getBaseAV());
        });
        getPlayers().forEach(AbstractCharacter::tryUltimate);
    }

    public int CyclesUsed() {
        return this.turbulence.numTurnsMetric;
    }
}
