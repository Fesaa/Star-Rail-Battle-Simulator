package art.ameliah.hsr.battleLogic.wave.pf;

import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.powers.PermPower;
import org.jetbrains.annotations.Nullable;

public interface ISurgingGrit {

    /**
     * Power to grant all enemies on activation
     * @return the power, the same instance will be re-used across all enemies
     */
    @Nullable
    PermPower getEnemyPower();

    /**
     * Called when Surging Grit goes into effect
     * <p></p>
     * There is no need to manually add powers here. But other buffs on activation can be appleid
     *
     * @param battle the battle
     */
    void apply(IBattle battle);

    /**
     * Called when Surging Grit leaves the battle
     * <p></p>
     * Remove all granted powers here
     *
     * @param battle the battle
     */
    void remove(IBattle battle);

}
