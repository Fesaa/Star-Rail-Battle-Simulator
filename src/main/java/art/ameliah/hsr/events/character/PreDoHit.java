package art.ameliah.hsr.events.character;

import art.ameliah.hsr.battleLogic.combat.hit.Hit;
import art.ameliah.hsr.events.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PreDoHit implements Event {

    private final Hit hit;

}
