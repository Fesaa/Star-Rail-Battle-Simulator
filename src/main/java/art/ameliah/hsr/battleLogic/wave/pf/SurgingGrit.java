package art.ameliah.hsr.battleLogic.wave.pf;

import art.ameliah.hsr.battleLogic.IBattle;

public interface SurgingGrit {

    /**
     * Called when Surging Grit goes into effect
     * @param battle the battle
     */
    void apply(IBattle battle);

    /**
     * Called when Surging Grit leaves the battle
     * @param battle the battle
     */
    void remove(IBattle battle);

}
