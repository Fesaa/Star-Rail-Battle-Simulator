package art.ameliah.hsr.game.pf.technicalityentrapment;

import art.ameliah.hsr.battleLogic.wave.pf.PfBattle;
import art.ameliah.hsr.battleLogic.wave.pf.PfWave;
import art.ameliah.hsr.battleLogic.wave.pf.PureFictionBuff;
import art.ameliah.hsr.characters.AbstractCharacter;

import java.util.List;

public class FirstHalf extends PfBattle {

    public FirstHalf(List<AbstractCharacter<?>> players, PureFictionBuff buff) {
        super(new ConcordantTruce(), buff, new SurgingGrit());
        this.setPlayerTeam(players);

        PfWave firstWave = new PfWave();
        // https://api.hakush.in/hsr/data/en/story/2011.json
        firstWave.addEnemies(1012030, 2012010, 300205006, 1012030, 2012010, 1012030, 2012010, 1012030, 2012010, 300205006, 1012030, 2012010, 1012030, 2012010, 1012030, 2012010, 300205006, 1012030, 2012010, 1012030, 2012010, 1012030, 2012010, 1012030, 2012010);

        this.addWave(firstWave);

    }


}
