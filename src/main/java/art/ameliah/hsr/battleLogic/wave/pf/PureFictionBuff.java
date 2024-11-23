package art.ameliah.hsr.battleLogic.wave.pf;

public interface PureFictionBuff {

    /**
     * Setup grit mechanic in the pf game.
     * @param battle the battle
     */
    void applyGritMechanic(PfBattle battle);

    /**
     * Apply the buff obtained when Surging Grit goes into effect
     * @param battle the battle
     */
    void applySurgingGritBuff(PfBattle battle);

    /**
     * Remove the buff received during Surging Grit
     * @param battle the battle
     */
    void removeSurgingGritBuff(PfBattle battle);
}
