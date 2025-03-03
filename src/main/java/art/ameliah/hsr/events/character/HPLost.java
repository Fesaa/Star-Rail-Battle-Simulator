package art.ameliah.hsr.events.character;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.events.CancelAbleEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Changing the amount variable will change the amount of HP lost
 */
@Setter
@Getter
@AllArgsConstructor
public class HPLost extends CancelAbleEvent {

    private BattleParticipant source;
    private float amount;

}
