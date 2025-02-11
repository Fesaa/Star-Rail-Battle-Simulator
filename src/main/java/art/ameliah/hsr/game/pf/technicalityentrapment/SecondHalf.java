package art.ameliah.hsr.game.pf.technicalityentrapment;

import art.ameliah.hsr.battleLogic.wave.pf.PfBattle;
import art.ameliah.hsr.battleLogic.wave.pf.PureFictionBuff;
import art.ameliah.hsr.characters.AbstractCharacter;

import java.util.List;

public class SecondHalf extends PfBattle {

    public SecondHalf(List<AbstractCharacter<?>> players, PureFictionBuff buff) {
        super(new ConcordantTruce(), buff, new SurgingGrit());

        this.setPlayerTeam(players);
    }

}
