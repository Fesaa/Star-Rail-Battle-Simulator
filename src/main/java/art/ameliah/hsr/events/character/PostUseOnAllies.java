package art.ameliah.hsr.events.character;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Event;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
public class PostUseOnAllies implements Event {

    private final Collection<AbstractCharacter<?>> allies;

    public PostUseOnAllies(AbstractCharacter<?> ally) {
        this.allies = List.of(ally);
    }

    public PostUseOnAllies(Collection<AbstractCharacter<?>> allies) {
        this.allies = allies;
    }

}
