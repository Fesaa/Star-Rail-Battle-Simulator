package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.combat.base.AbstractAttack;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;


@RequiredArgsConstructor
public class AttackStart implements Loggable {

    private final AbstractAttack<?, ?, ?, ?> attack;

    @Override
    public String asString() {
        List<?> targets = this.attack.getTargets().stream().sorted(Comparator.comparing((BattleParticipant e) -> e.getName())).toList();
        return String.format("%s started attacking [%s] with %s", this.attack.getSource(), targets, this.attack.getTypes());
    }

    @Override
    public String prefix() {
        return "\n";
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
