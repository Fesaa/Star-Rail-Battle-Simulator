package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.combat.Hit;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public record HitResult(Hit hit) implements Loggable {

    @Override
    public String asString() {
        return String.format("%s hit %s(%.1f) for %.3f damage",
                this.hit.getSource().getName(), this.hit.getTarget().getName(), this.hit.getTarget().getCurrentHp(), this.hit.finalDmg());
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
