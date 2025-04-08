package art.ameliah.hsr.events.character;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.remembrance.Memosprite;
import art.ameliah.hsr.events.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostMemospriteAttack implements Event {

    private final AttackLogic attack;
    private final Memosprite<?, ?> memo;

}
