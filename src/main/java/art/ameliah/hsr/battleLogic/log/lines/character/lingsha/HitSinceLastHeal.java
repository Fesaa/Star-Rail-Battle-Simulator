package art.ameliah.hsr.battleLogic.log.lines.character.lingsha;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;

public class HitSinceLastHeal implements Loggable {

    public final AbstractCharacter<?> character;
    public final int timesHit;

    public HitSinceLastHeal(AbstractCharacter<?> character, int timesHit) {
        this.character = character;
        this.timesHit = timesHit;
    }

    @Override
    public String asString() {
        return String.format("%s has been hit %d times since last heal", character.name, timesHit);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
