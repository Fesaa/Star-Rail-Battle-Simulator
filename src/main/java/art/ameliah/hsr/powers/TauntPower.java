package art.ameliah.hsr.powers;

import art.ameliah.hsr.characters.AbstractCharacter;

public class TauntPower extends AbstractPower {
    public final AbstractCharacter<?> taunter;

    public TauntPower(AbstractCharacter<?> taunter) {
        this.taunter = taunter;
        this.setName(this.getClass().getSimpleName());
    }

}
