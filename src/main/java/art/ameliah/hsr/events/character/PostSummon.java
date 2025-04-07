package art.ameliah.hsr.events.character;

import art.ameliah.hsr.characters.remembrance.Memosprite;
import art.ameliah.hsr.events.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostSummon implements Event {

    private final Memosprite<?, ?> memosprite;

}
