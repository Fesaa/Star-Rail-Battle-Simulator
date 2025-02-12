package art.ameliah.hsr.characters;


import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.IBattle;

public class AbstractSummon<O extends AbstractCharacter<O> & Summoner> extends AbstractEntity {

    protected final O summoner;

    public AbstractSummon(O summoner) {
        this.summoner = summoner;
    }

    @Override
    public IBattle getBattle() {
        return this.summoner.getBattle();
    }
}
