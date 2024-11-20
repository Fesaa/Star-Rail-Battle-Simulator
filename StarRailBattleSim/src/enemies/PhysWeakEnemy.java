package enemies;

import characters.ElementType;

public class PhysWeakEnemy extends DumbEnemy {

    public PhysWeakEnemy(int index, int doubleActionCooldown) {
        super("PhysWeakEnemy" + index, EnemyType.Elite, 301193, 718, 1150, 150, 100, doubleActionCooldown);
        setRes(ElementType.PHYSICAL, 0);
        this.addWeakness(ElementType.PHYSICAL);
        this.speedPriority = index;
    }
}
