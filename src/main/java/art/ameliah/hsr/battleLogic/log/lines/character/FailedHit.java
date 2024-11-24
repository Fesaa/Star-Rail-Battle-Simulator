package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.combat.Hit;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public record FailedHit(Hit hit) implements Loggable {
    @Override
    public String asString() {
        return String.format("%s failed to hit %s for %s dmg",
                this.hit.getSource(), this.hit.getTarget().name, this.hit.finalDmg());
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
