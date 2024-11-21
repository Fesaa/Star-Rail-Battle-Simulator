package art.ameliah.hsr.battleLogic.log.lines.character.aventurine;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.aventurine.Aventurine;

public class UseBlindBet implements Loggable {

    public final AbstractCharacter<?> character;
    public final int initialBlindBet;
    public final int blindBetCounter;

    public UseBlindBet(Aventurine aventurine, int initialBlindBet, int blindBetCounter) {
        this.character = aventurine;
        this.initialBlindBet = initialBlindBet;
        this.blindBetCounter = blindBetCounter;
    }



    @Override
    public String asString() {
        return String.format("%s used Follow Up (%,d -> %,d)", character.name, initialBlindBet, this.blindBetCounter);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
