package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;

public class Attacked implements Loggable {

    public final AbstractEnemy source;
    public final AbstractCharacter<?> target;

    public Attacked(AbstractEnemy source, AbstractCharacter<?> target) {
        this.source = source;
        this.target = target;
    }


    @Override
    public String asString() {
        return source.name + " attacked " + target.name;
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
