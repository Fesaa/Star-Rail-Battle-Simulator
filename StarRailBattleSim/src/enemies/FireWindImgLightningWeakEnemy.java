package enemies;

import characters.AbstractCharacter;

public class FireWindImgLightningWeakEnemy extends DumbEnemy {

    public FireWindImgLightningWeakEnemy(int index, int doubleActionCooldown) {
        super("FireWindImgLightningWeakEnemy" + index, EnemyType.Elite, 301193, 718, 1150, 150, 100, doubleActionCooldown);
        setRes(AbstractCharacter.ElementType.FIRE, 0);
        setRes(AbstractCharacter.ElementType.WIND, 0);
        setRes(AbstractCharacter.ElementType.IMAGINARY, 0);
        setRes(AbstractCharacter.ElementType.LIGHTNING, 0);
        this.addWeakness(AbstractCharacter.ElementType.FIRE);
        this.addWeakness(AbstractCharacter.ElementType.WIND);
        this.addWeakness(AbstractCharacter.ElementType.IMAGINARY);
        this.addWeakness(AbstractCharacter.ElementType.LIGHTNING);
        this.speedPriority = index;
    }
}
