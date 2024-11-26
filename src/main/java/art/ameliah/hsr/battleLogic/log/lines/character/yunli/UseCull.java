package art.ameliah.hsr.battleLogic.log.lines.character.yunli;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.yunli.Yunli;

public class UseCull implements Loggable {

    public final AbstractCharacter<?> yunli;

    public UseCull(Yunli yunli) {
        this.yunli = yunli;
    }

    @Override
    public String asString() {
        return this.yunli.getName() + " used Cull";
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
