package amelia;

import characters.AbstractCharacter;
import enemies.AbstractEnemy;

public class Hoolay extends AbstractEnemy {


    public Hoolay() {
        super("Hoolay", 752984, 718, 1150, 264, 95, 720, 0);
        setRes(AbstractCharacter.ElementType.ICE, 20);
        setRes(AbstractCharacter.ElementType.LIGHTNING, 20);
        setRes(AbstractCharacter.ElementType.QUANTUM, 20);
        setRes(AbstractCharacter.ElementType.IMAGINARY, 20);
        weaknessMap.add(AbstractCharacter.ElementType.WIND);
        weaknessMap.add(AbstractCharacter.ElementType.PHYSICAL);
        weaknessMap.add(AbstractCharacter.ElementType.FIRE);
        this.speedPriority = 0;
    }
}
