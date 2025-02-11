package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;

public record CharacterDeath(AbstractCharacter<?> character, BattleParticipant source, String reason) implements Loggable {
    @Override
    public String asString() {
        return String.format("%s died because of %s: %s", character.getName(), source.getName(), reason);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
