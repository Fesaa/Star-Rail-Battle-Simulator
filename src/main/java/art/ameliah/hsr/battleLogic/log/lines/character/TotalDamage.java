package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;

import java.util.ArrayList;
import java.util.List;

public class TotalDamage implements Loggable {

    public final AbstractCharacter<?> character;
    public final List<DamageType> types;
    public final int totalDamage;

    public TotalDamage(AbstractCharacter<?> character, List<DamageType> types, int totalDamage) {
        this.character = character;
        this.types = new ArrayList<>(types);
        this.totalDamage = totalDamage;
    }

    @Override
    public String asString() {
        return String.format("Total Damage: %,d", this.totalDamage);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
