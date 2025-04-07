package art.ameliah.hsr.events.character;

import art.ameliah.hsr.battleLogic.combat.result.HitResult;
import art.ameliah.hsr.events.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostDoHit implements Event {

    private final HitResult hit;

}
