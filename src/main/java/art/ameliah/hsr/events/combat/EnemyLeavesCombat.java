package art.ameliah.hsr.events.combat;

import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EnemyLeavesCombat implements Event {

    private final AbstractEnemy enemy;
    private final int idx;

}
