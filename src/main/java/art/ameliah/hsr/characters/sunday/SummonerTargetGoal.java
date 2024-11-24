package art.ameliah.hsr.characters.sunday;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.AbstractSummoner;
import art.ameliah.hsr.characters.goal.AllyTargetGoal;

import java.util.Optional;

public class SummonerTargetGoal extends AllyTargetGoal<Sunday> {

    public SummonerTargetGoal(Sunday character) {
        super(character);
    }

    @Override
    public Optional<AbstractCharacter<?>> getTarget() {
        return getBattle().getPlayers()
                .stream()
                .filter(c -> c instanceof AbstractSummoner<?>)
                .findFirst();
    }
}
