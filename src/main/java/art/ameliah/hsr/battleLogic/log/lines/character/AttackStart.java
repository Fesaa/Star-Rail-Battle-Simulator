package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.combat.base.AbstractAttack;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class AttackStart implements Loggable {

    private final AbstractAttack<?, ?, ?, ?> attack;

    @Override
    public String asString() {
        return String.format("%s started attacking [%s] with %s", this.attack.getSource(), this.attack.getTargets(), this.attack.getTypes());
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
