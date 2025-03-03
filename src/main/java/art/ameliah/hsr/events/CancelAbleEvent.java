package art.ameliah.hsr.events;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class CancelAbleEvent implements Event {

    private boolean canceled = false;

}
