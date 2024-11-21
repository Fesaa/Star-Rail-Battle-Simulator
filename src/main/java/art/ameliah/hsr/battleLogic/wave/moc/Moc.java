package art.ameliah.hsr.battleLogic.wave.moc;

import art.ameliah.hsr.battleLogic.log.LogSupplier;

public class Moc {

    private static final int MOC_START_AV = 29 * 100 + 150;

    private final MocBattle firstHalf;
    private final MocBattle secondHalf;

    public Moc(MocBattle firstHalf, MocBattle secondHalf) {
        this.firstHalf = firstHalf;
        this.secondHalf = secondHalf;
    }

    public void setBattleLogger(LogSupplier logger) {
        this.firstHalf.setLogger(logger);
        this.secondHalf.setLogger(logger);
    }

    public void Start() {
        this.firstHalf.Start(MOC_START_AV);
        int cyclesUsed = this.firstHalf.CyclesUsed();

        if (this.secondHalf.getPlayers().isEmpty()) {
            throw new IllegalStateException("No players for second half");
        }

        int avSecondHalf = MOC_START_AV - 100 * cyclesUsed;
        this.secondHalf.Start(avSecondHalf);
    }
}
