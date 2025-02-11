package art.ameliah.hsr.battleLogic.wave.pf;

import art.ameliah.hsr.battleLogic.log.LogSupplier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PureFiction {

    private static final int PF_START_AV = 150 + 3 * 100;

    private final PfBattle firstHalf;
    private final PfBattle secondHalf;

    public void setBattleLogger(LogSupplier logger) {
        this.firstHalf.setLogger(logger);
        this.secondHalf.setLogger(logger);
    }

    // TODO: Add points, but I'm not sure how they really work ya
    public void Start() {
        this.firstHalf.Start(PF_START_AV);
        this.secondHalf.Start(PF_START_AV);
    }

}
