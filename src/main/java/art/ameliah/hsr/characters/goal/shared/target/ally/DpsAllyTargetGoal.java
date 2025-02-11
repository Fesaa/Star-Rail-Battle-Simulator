package art.ameliah.hsr.characters.goal.shared.target.ally;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.AllyTargetGoal;

import java.util.Optional;

public class DpsAllyTargetGoal<C extends AbstractCharacter<C>> extends AllyTargetGoal<C> {

    public DpsAllyTargetGoal(C character) {
        super(character);
    }

    @Override
    public Optional<AbstractCharacter<?>> getTarget() {
        return getBattle().getPlayers()
                .stream()
                .filter(p -> p.isDPS)
                .findFirst();
    }
}
