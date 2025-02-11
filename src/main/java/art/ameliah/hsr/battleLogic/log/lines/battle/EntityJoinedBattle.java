package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public record EntityJoinedBattle(AbstractEntity entity) implements Loggable {
    @Override
    public String asString() {
        return this.entity.getName() + " has joined the battle";
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
