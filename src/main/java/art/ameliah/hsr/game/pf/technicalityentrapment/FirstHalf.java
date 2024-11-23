package art.ameliah.hsr.game.pf.technicalityentrapment;

import art.ameliah.hsr.battleLogic.wave.pf.PfBattle;
import art.ameliah.hsr.battleLogic.wave.pf.PureFictionBuff;

public class FirstHalf extends PfBattle {

    public FirstHalf(PureFictionBuff buff) {
        super(new ConcordantTruce(), buff, new SurgingGrit());
    }


}
