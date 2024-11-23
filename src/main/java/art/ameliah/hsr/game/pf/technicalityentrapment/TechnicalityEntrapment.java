package art.ameliah.hsr.game.pf.technicalityentrapment;


import art.ameliah.hsr.battleLogic.wave.pf.PureFiction;

public class TechnicalityEntrapment extends PureFiction {

    public TechnicalityEntrapment() {
        super(new FirstHalf(new FalsePromises()), new SecondHalf(new EmptyAir()));
    }
}
