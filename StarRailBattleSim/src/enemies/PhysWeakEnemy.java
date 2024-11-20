package enemies;

import characters.AbstractCharacter;

public class PhysWeakEnemy extends DumbEnemy {

    public PhysWeakEnemy(int index, int doubleActionCooldown) {
        super("PhysWeakEnemy" + index, EnemyType.Elite, 301193, 718, 1150, 150, 100, doubleActionCooldown);
        setRes(AbstractCharacter.ElementType.PHYSICAL, 0);
        this.addWeakness(AbstractCharacter.ElementType.PHYSICAL);
        this.speedPriority = index;
    }
}
