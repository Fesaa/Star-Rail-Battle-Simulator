package art.ameliah.hsr.powers;

import art.ameliah.hsr.characters.AbstractCharacter;

public class TauntPower extends AbstractPower {
    public AbstractCharacter taunter;

    public TauntPower(AbstractCharacter taunter) {
        this.taunter = taunter;
        this.name = this.getClass().getSimpleName();
    }

}
