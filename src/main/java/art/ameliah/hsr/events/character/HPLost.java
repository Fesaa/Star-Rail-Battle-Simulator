package art.ameliah.hsr.events.character;

import art.ameliah.hsr.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Changing the amount variable will change the amount of HP lost
 */
@Setter
@Getter
@AllArgsConstructor
public class HPLost implements Event {

    private float amount;

}
