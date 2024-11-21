package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class Attacked implements Loggable {

    public final AbstractEntity source;
    public final List<? extends  AbstractEntity> targets;
    public final float damage;

    public Attacked(AbstractEntity source, List<? extends AbstractEntity> targets, float damage) {
        this.source = source;
        this.targets = targets;
        this.damage = damage;
    }

    public Attacked(AbstractEntity source, AbstractEntity target, float damage) {
        this.source = source;
        this.targets = List.of(target);
        this.damage = damage;
    }


    @Override
    public String asString() {
        return source.name + " attacked " + this.targets.stream()
                .map(e -> e.name + "(" + e.getCurrentHp() + ")")
                .collect(Collectors.joining(","))
                + " for " + damage + " damage";
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
