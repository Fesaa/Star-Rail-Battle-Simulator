package art.ameliah.hsr.battleLogic.log.lines.character;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.combat.base.AbstractAttack;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AttackEnd implements Loggable {

    private final AbstractAttack<?, ?, ?, ?> attack;

    @Override
    public String asString() {
        String targets = this.attack.getTargets().stream().map(BattleParticipant::getName).sorted(String::compareTo).collect(Collectors.joining(","));
        String types = this.attack.getTypes().stream().map(Objects::toString).collect(Collectors.joining(","));
        return String.format("%s finished attacking %s with %s", this.attack.getSource(), targets, types);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
