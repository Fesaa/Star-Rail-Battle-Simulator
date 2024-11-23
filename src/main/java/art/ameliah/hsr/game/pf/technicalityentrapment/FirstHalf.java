package art.ameliah.hsr.game.pf.technicalityentrapment;

import art.ameliah.hsr.battleLogic.wave.pf.PfBattle;
import art.ameliah.hsr.battleLogic.wave.pf.PfWave;
import art.ameliah.hsr.battleLogic.wave.pf.PureFictionBuff;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.game.cosmos.LordyTrashcan;
import art.ameliah.hsr.enemies.game.jarilovi.AutomatonBeetle;
import art.ameliah.hsr.enemies.game.luofu.EntrancedIngeniumIlluminationDragonfish;

import java.util.List;

public class FirstHalf extends PfBattle {

    public FirstHalf(List<AbstractCharacter<?>> players, PureFictionBuff buff) {
        super(new ConcordantTruce(), buff, new SurgingGrit());
        this.setPlayerTeam(players);

        PfWave firstWave = new PfWave(null, List.of(
                new AutomatonBeetle(),
                new EntrancedIngeniumIlluminationDragonfish(),
                new LordyTrashcan(),
                new AutomatonBeetle(),
                new EntrancedIngeniumIlluminationDragonfish()
        ));
        firstWave.addMinionType(AutomatonBeetle.class, 11, AutomatonBeetle::new);
        firstWave.addMinionType(EntrancedIngeniumIlluminationDragonfish.class, 10, EntrancedIngeniumIlluminationDragonfish::new);
        firstWave.addMinionType(LordyTrashcan.class, 3, LordyTrashcan::new);

        this.addWave(firstWave);
    }


}
