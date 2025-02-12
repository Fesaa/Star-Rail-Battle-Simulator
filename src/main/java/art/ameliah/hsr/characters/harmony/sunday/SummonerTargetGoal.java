package art.ameliah.hsr.characters.harmony.sunday;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.AbstractSummoner;
import art.ameliah.hsr.characters.goal.AllyTargetGoal;
import art.ameliah.hsr.characters.remembrance.Memomaster;

import java.util.Optional;

public class SummonerTargetGoal extends AllyTargetGoal<Sunday> {

    public SummonerTargetGoal(Sunday character) {
        super(character);
    }

    @Override
    public Optional<AbstractCharacter<?>> getTarget() {
        var memo = getBattle().getPlayers()
                .stream()
                .filter(c -> c instanceof Memomaster<?>)
                .findFirst();
        if (memo.isPresent()) {
            return memo;
        }

        return getBattle().getPlayers()
                .stream()
                .filter(c -> c instanceof AbstractSummoner<?>)
                .findFirst();
    }
}
