package enemies;

import characters.AbstractCharacter;

public class WindWeakEnemy extends DumbEnemy {

    public WindWeakEnemy(int index, int doubleActionCooldown) {
        super("WindWeakEnemy" + index, EnemyType.Elite, 301193, 718, 1150, 150, 100, doubleActionCooldown);
        setRes(AbstractCharacter.ElementType.WIND, 0);
        this.addWeakness(AbstractCharacter.ElementType.WIND);
        this.speedPriority = index;
    }
}
