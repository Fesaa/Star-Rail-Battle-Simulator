package enemies;

import characters.ElementType;

public class PhysFireWeakEnemy extends DumbEnemy {

    public PhysFireWeakEnemy(int index, int doubleActionCooldown) {
        super("PhysFireWeakEnemy" + index, EnemyType.Elite, 301193, 718, 1150, 150, 100, doubleActionCooldown);
        setRes(ElementType.PHYSICAL, 0);
        setRes(ElementType.FIRE, 0);
        this.addWeakness(ElementType.PHYSICAL);
        this.addWeakness(ElementType.FIRE);
        this.speedPriority = index;
    }
}
