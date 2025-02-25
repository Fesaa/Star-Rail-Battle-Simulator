package art.ameliah.hsr.events.enemy;

import art.ameliah.hsr.battleLogic.combat.hit.Hit;
import art.ameliah.hsr.events.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PreHit implements Event {

    private final Hit hit;

}
