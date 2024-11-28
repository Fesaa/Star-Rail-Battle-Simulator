package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.combat.result.HitResult;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public record HitResultLine(HitResult hit) implements Loggable {

    @Override
    public String asString() {
        return String.format("%s hit %s for %.3f damage (%.3f -> %.3f)",
                this.hit.getHit().getSource().getName(),
                this.hit.getHit().getTarget().toString(),
                this.hit.getHit().finalDmg(),
                this.hit.getEnemy().getCurrentHp().get() + this.hit.getDmgDealt(),
                this.hit.getEnemy().getCurrentHp().get()
        );
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
