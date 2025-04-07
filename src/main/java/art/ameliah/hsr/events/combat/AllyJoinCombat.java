package art.ameliah.hsr.events.combat;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AllyJoinCombat implements Event {

    private final AbstractCharacter<?> ally;

}
