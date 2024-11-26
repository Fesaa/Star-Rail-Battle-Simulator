package art.ameliah.hsr.battleLogic.log.lines.character.lingsha;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;

public record HitSinceLastHeal(AbstractCharacter<?> character, int timesHit) implements Loggable {

    @Override
    public String asString() {
        return String.format("%s has been hit %,d times since last heal", character.getName(), timesHit);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
