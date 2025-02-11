package art.ameliah.hsr.game.pf.technicalityentrapment;


import art.ameliah.hsr.battleLogic.wave.pf.PureFiction;
import art.ameliah.hsr.characters.AbstractCharacter;

import java.util.List;

public class TechnicalityEntrapment extends PureFiction {

    public TechnicalityEntrapment(List<AbstractCharacter<?>> players1, List<AbstractCharacter<?>> players2) {
        super(new FirstHalf(players1, new FalsePromises()), new SecondHalf(players2, new EmptyAir()));
    }
}
