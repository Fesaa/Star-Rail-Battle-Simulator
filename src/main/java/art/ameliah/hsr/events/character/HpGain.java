package art.ameliah.hsr.events.character;

import art.ameliah.hsr.events.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HpGain implements Event {

    private final float amount;
    private final float overflow;

}
