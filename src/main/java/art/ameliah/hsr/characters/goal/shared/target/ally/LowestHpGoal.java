package art.ameliah.hsr.characters.goal.shared.target.ally;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.AllyTargetGoal;

import java.util.Optional;

public class LowestHpGoal<C extends AbstractCharacter<C>> extends AllyTargetGoal<C> {
    public LowestHpGoal(C character) {
        super(character);
    }

    @Override
    public Optional<AbstractCharacter<?>> getTarget() {
        return getBattle().getPlayers().stream()
                .min((o1, o2) ->
                        Float.compare(o1.getCurrentHp().get(), o2.getCurrentHp().get()));
    }
}
