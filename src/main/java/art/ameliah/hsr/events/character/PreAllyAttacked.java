package art.ameliah.hsr.events.character;

import art.ameliah.hsr.battleLogic.combat.enemy.EnemyAttackLogic;
import art.ameliah.hsr.events.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PreAllyAttacked implements Event {

    private final EnemyAttackLogic attack;

}
