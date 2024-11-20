package battleLogic.wave.moc;

public class Moc {

    private static final int MOC_START_AV = 29 * 100 + 150;

    private final MocBattle firstHalf;
    private final MocBattle secondHalf;

    public Moc(MocBattle firstHalf, MocBattle secondHalf) {
        this.firstHalf = firstHalf;
        this.secondHalf = secondHalf;
    }

    public void Start() {
        this.firstHalf.Start(MOC_START_AV);
        int cyclesUsed = this.firstHalf.CyclesUsed();
        int avSecondHalf = MOC_START_AV - 100 * cyclesUsed;
        this.secondHalf.Start(avSecondHalf);
    }
}
