package art.ameliah.hsr.battleLogic.log.lines.battle.pf;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SurgingGritState implements Loggable {

    private final State state;

    @Override
    public String asString() {
        return "Surging grit has " + this.state.s;
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }

    public enum State {
        START("started"), END("ended");

        private final String s;

        State(String s) {
            this.s = s;
        }
    }
}
