package art.ameliah.hsr.events.enemy;

import art.ameliah.hsr.battleLogic.combat.enemy.EnemyAttackLogic;
import art.ameliah.hsr.events.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostEnemyAttack implements Event {

    private final EnemyAttackLogic attack;

}
