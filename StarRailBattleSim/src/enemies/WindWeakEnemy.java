package enemies;

import characters.ElementType;

public class WindWeakEnemy extends DumbEnemy {

    public WindWeakEnemy(int index, int doubleActionCooldown) {
        super("WindWeakEnemy" + index, EnemyType.Elite, 301193, 718, 1150, 150, 100, doubleActionCooldown);
        setRes(ElementType.WIND, 0);
        this.addWeakness(ElementType.WIND);
        this.speedPriority = index;
    }
}
