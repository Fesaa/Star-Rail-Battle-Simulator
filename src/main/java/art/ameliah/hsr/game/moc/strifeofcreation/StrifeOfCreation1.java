package art.ameliah.hsr.game.moc.strifeofcreation;

import art.ameliah.hsr.battleLogic.wave.moc.MocBattle;
import art.ameliah.hsr.battleLogic.wave.moc.MocWave;
import art.ameliah.hsr.enemies.game.jarilovi.SeniorStaffTeamLeader;

public class StrifeOfCreation1 extends MocBattle {
    public StrifeOfCreation1() {
        super(new Turbulence());

        this.addWave(new MocWave(
                new SeniorStaffTeamLeader(1117305, 626, 1150, 158.40f)
        ));
    }
}
