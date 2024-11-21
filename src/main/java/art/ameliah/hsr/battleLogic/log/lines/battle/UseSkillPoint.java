package art.ameliah.hsr.battleLogic.log.lines.battle;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;

public record UseSkillPoint(AbstractCharacter<?> character, int amount, int from, int to) implements Loggable {


    @Override
    public String asString() {
        return String.format("%s used %,d Skill Point(s) (%,d -> %,d)", this.character.name, this.amount, this.from, this.to);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
