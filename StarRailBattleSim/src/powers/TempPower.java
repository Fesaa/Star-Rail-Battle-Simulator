package powers;

import relics.RelicStats;

public class TempPower extends AbstractPower {

    public TempPower() {

    }

    /**
     * Create a new TempPower with the given stat and value, this is a helper method to make it easier to create PermPowers
     * The AbstractPower system is created a bit awkwardly, so this is a workaround
     * @param stat the stat to create the PermPower for, is forwarded most fittingly
     * @param value the value of the stat
     * @param turns the amount of turns the power should last
     * @param name the name of the power
     * @return the created TempPower
     */
    public static TempPower create(PowerStat stat, float value, int turns, String name) {
        TempPower power = new TempPower();
        power.name = name;
        power.turnDuration = turns;
        power.setStat(stat, value);
        return power;
    }

}
