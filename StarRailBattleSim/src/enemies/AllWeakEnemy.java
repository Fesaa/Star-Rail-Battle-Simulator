package enemies;

import characters.AbstractCharacter;

public class AllWeakEnemy extends DumbEnemy {

    public AllWeakEnemy(int index, int doubleActionCooldown) {
        super("AllWeakEnemy" + index, EnemyType.Elite, 301193, 718, 1150, 150, 100, doubleActionCooldown);
        setRes(AbstractCharacter.ElementType.FIRE, 0);
        setRes(AbstractCharacter.ElementType.WIND, 0);
        setRes(AbstractCharacter.ElementType.IMAGINARY, 0);
        setRes(AbstractCharacter.ElementType.LIGHTNING, 0);
        setRes(AbstractCharacter.ElementType.PHYSICAL, 0);
        setRes(AbstractCharacter.ElementType.QUANTUM, 0);
        setRes(AbstractCharacter.ElementType.ICE, 0);
        this.addWeakness(AbstractCharacter.ElementType.FIRE);
        this.addWeakness(AbstractCharacter.ElementType.WIND);
        this.addWeakness(AbstractCharacter.ElementType.IMAGINARY);
        this.addWeakness(AbstractCharacter.ElementType.LIGHTNING);
        this.addWeakness(AbstractCharacter.ElementType.PHYSICAL);
        this.addWeakness(AbstractCharacter.ElementType.QUANTUM);
        this.addWeakness(AbstractCharacter.ElementType.ICE);
        this.speedPriority = index;
    }
}
