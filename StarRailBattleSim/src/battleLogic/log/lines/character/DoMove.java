package battleLogic.log.lines.character;

import battleLogic.log.Loggable;
import battleLogic.log.Logger;
import characters.AbstractCharacter;
import characters.MoveType;

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
            return String.format("%s used Ultimate (%.3f -> %.3f)", this.character.name, this.initialEnergy, this.currentEnergy);
        }

        return this.character.name + " used " + this.moveType;
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
