package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AttackEnd implements Loggable {

    private final Attack attack;

    @Override
    public String asString() {
        String targets = this.attack.getTargets().stream().map(t -> t.getName()).collect(Collectors.joining(","));
        String types = this.attack.getTypes().stream().map(Objects::toString).collect(Collectors.joining(","));
        return String.format("%s finished attacking %s with %s", this.attack.getSource(), targets, types);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
