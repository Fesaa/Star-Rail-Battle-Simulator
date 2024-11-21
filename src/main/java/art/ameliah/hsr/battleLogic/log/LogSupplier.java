package art.ameliah.hsr.battleLogic.log;

import art.ameliah.hsr.battleLogic.IBattle;

@FunctionalInterface
public interface LogSupplier {

    Logger get(IBattle battle);

}
