package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;


@RequiredArgsConstructor
public class AttackStart implements Loggable {

    private final Attack attack;

    @Override
    public String asString() {
        String targets = this.attack.getTargets().stream().map(t -> t.name).collect(Collectors.joining(","));
        return String.format("%s started attacking %s", this.attack.getSource(), targets);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
