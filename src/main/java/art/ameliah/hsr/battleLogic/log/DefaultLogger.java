package art.ameliah.hsr.battleLogic.log;

import art.ameliah.hsr.battleLogic.IBattle;

import java.io.PrintStream;

/**
 * Default implementation of the Logger interface.
 * This logger will print everything to the passed PrintStream
 */
public class DefaultLogger extends Logger {

    public DefaultLogger(IBattle battle, PrintStream out) {
        super(battle, out);
    }

    public DefaultLogger(IBattle battle) {
        super(battle);
    }

    private String prefix() {
        if (this.inBattle()) {
            return this.battle.prefix();
        }
        return "";
    }

    @Override
    protected void log(Loggable loggable) {
        this.out.println(loggable.prefix() + prefix() + loggable.asString());
    }
}
