package art.ameliah.hsr.events.combat;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.events.CancelAbleEvent;
import art.ameliah.hsr.events.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeathEvent extends CancelAbleEvent {

    private final BattleParticipant source;

}
