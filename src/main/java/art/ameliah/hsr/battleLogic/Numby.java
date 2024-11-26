package art.ameliah.hsr.battleLogic;

import art.ameliah.hsr.characters.topaz.Topaz;

public class Numby extends AbstractSummon<Topaz> {
    public static final String NAME = "Numby";

    public Numby(Topaz owner) {
        super(owner);
        this.baseSpeed = 80;
        this.name = name;
    }

    public void takeTurn() {
        super.takeTurn();
        this.summoner.getNumbyAttacks().increment();
        this.summoner.numbyAttack();
    }

    public void AdvanceForward() {
        float initialAV = getBattle().getActionValueMap().get(this);
        if (initialAV > 0) {
            speedPriority = 0;
            this.summoner.getNumbyAdvancedTimes().increment();
            getBattle().AdvanceEntity(this, 50);
            this.summoner.getNumbyAVAdvances().increase((int) Math.abs(initialAV - getBattle().getActionValueMap().get(this)));
        } else {
            this.summoner.getWastedNumbyAdvances().increment();
        }
    }
}
