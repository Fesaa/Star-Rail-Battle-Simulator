package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;

public record BreakDamageHitResult(AbstractCharacter<?> source, AbstractEnemy target, double calculatedDamage,
                                   double baseDamage, double breakEffectMultiplier, double defMultiplier,
                                   double resMultiplier, double damageTakenMultiplier,
                                   double toughnessMultiplier) implements Loggable {

    @Override
    public String asString() {
        return String.format("%s hit %s for %.3f Break damage - Base Damage: %.3f, Break Effect Multiplier: %.3f, Defense Multiplier: %.3f, Res Multiplier: %.3f, Damage Vuln Multiplier: %.3f, Toughness Multiplier: %.3f",
                source.name, target.name, calculatedDamage, baseDamage, breakEffectMultiplier, defMultiplier, resMultiplier,
                damageTakenMultiplier, toughnessMultiplier);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }

}
