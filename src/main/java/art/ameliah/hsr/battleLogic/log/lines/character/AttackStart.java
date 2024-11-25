package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class AttackStart implements Loggable {

    private final Attack attack;

    @Override
    public String asString() {
        return String.format("%s started attacking [%s] with %s", this.attack.getSource(),  this.attack.getTargets(), this.attack.getTypes());
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
