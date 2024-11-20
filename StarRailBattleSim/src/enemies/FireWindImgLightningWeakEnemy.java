package enemies;

import characters.ElementType;

public class FireWindImgLightningWeakEnemy extends DumbEnemy {

    public FireWindImgLightningWeakEnemy(int index, int doubleActionCooldown) {
        super("FireWindImgLightningWeakEnemy" + index, EnemyType.Elite, 301193, 718, 1150, 150, 100, doubleActionCooldown);
        setRes(ElementType.FIRE, 0);
        setRes(ElementType.WIND, 0);
        setRes(ElementType.IMAGINARY, 0);
        setRes(ElementType.LIGHTNING, 0);
        this.addWeakness(ElementType.FIRE);
        this.addWeakness(ElementType.WIND);
        this.addWeakness(ElementType.IMAGINARY);
        this.addWeakness(ElementType.LIGHTNING);
        this.speedPriority = index;
    }
}
