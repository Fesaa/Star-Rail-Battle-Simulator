package art.ameliah.hsr.events.enemy;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.events.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PreEnemyAttacked implements Event {

    private final AttackLogic attack;

}
