package art.ameliah.hsr.events.enemy;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.events.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WeaknessBreakEvent implements Event {

    private final BattleParticipant source;

}
