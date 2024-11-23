package art.ameliah.hsr.battleLogic.wave.moc;

import art.ameliah.hsr.battleLogic.wave.Wave;
import art.ameliah.hsr.battleLogic.wave.WavedBattle;
import art.ameliah.hsr.characters.AbstractCharacter;

public class MocBattle extends WavedBattle {

    private final MocTurbulence turbulence;

    public MocBattle(MocTurbulence turbulence) {
        this.turbulence = turbulence;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.actionValueMap.put(this.turbulence, this.turbulence.getBaseAV());
        this.turbulence.setBattle(this);
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
        this.actionValueMap.forEach((k, _) -> this.actionValueMap.put(k, k.getBaseAV()));
        getPlayers().forEach(AbstractCharacter::tryUltimate);
    }

    public int cyclesUsed() {
        return this.turbulence.numTurnsMetric;
    }
}
