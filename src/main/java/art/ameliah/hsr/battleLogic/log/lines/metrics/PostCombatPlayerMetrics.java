package art.ameliah.hsr.battleLogic.log.lines.metrics;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;

public class PostCombatPlayerMetrics implements Loggable {

    public final AbstractCharacter<?> player;

    public PostCombatPlayerMetrics(AbstractCharacter<?> player) {
        this.player = player;
    }

    @Override
    public String asString() {
        return this.player.getMetrics() + "\n";
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
