package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.MoveType;

public class DoMove implements Loggable {
    public final AbstractCharacter<?> character;
    public final MoveType moveType;
    public final float initialEnergy;
    public final float currentEnergy;

    public DoMove(AbstractCharacter<?> character, MoveType moveType) {
        this.character = character;
        this.moveType = moveType;
        this.initialEnergy = -1;
        this.currentEnergy = -1;
    }

    public DoMove(AbstractCharacter<?> character, MoveType moveType, float initialEnergy, float currentEnergy) {
        this.character = character;
        this.moveType = moveType;
        this.initialEnergy = initialEnergy;
        this.currentEnergy = currentEnergy;
    }

    @Override
    public String asString() {
        if (this.moveType.equals(MoveType.ULTIMATE)) {
            return String.format("%s used Ultimate (%.3f -> %.3f)", this.character.getName(), this.initialEnergy, this.currentEnergy);
        }

        return this.character.getName() + " used " + this.moveType;
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
