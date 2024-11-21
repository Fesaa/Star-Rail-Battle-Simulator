package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

public class Attacked implements Loggable {

    public final AbstractEntity source;
    public final AbstractEntity target;
    public final float damage;

    public Attacked(AbstractEntity source, AbstractEntity target, float damage) {
        this.source = source;
        this.target = target;
        this.damage = damage;
    }


    @Override
    public String asString() {
        return source.name + " attacked "
                + String.format("%s (%.0f)", target.name, target.getCurrentHp())
                + " for " + damage + " damage";
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
