package art.ameliah.hsr.events.character;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HPGain implements Event {

    private final BattleParticipant source;
    private final AbstractCharacter<?> healed;
    private final float amount;
    private final float overflow;

}
