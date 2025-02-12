package art.ameliah.hsr.characters.harmony.sunday;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.Summoner;
import art.ameliah.hsr.characters.goal.AllyTargetGoal;

import java.util.Optional;

public class SummonerTargetGoal extends AllyTargetGoal<Sunday> {

    public SummonerTargetGoal(Sunday character) {
        super(character);
    }

    @Override
    public Optional<AbstractCharacter<?>> getTarget() {
        var dpsSummoner = getBattle().getPlayers()
                .stream()
                .filter(c -> c instanceof Summoner)
                .filter(c -> c.isDPS)
                .findFirst();

        if (dpsSummoner.isPresent()) {
            return dpsSummoner;
        }

        return getBattle().getPlayers()
                .stream()
                .filter(c -> c instanceof Summoner)
                .findFirst();
    }
}
