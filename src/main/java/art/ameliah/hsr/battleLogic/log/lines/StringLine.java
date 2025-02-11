package art.ameliah.hsr.battleLogic.log.lines;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StringLine implements Loggable {

    private final String line;

    @Override
    public String asString() {
        return this.line;
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
