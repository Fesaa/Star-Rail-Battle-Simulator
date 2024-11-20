package enemies;

import characters.AbstractCharacter;

public class PhysFireWeakEnemy extends DumbEnemy {

    public PhysFireWeakEnemy(int index, int doubleActionCooldown) {
        super("PhysFireWeakEnemy" + index, EnemyType.Elite, 301193, 718, 1150, 150, 100, doubleActionCooldown);
        setRes(AbstractCharacter.ElementType.PHYSICAL, 0);
        setRes(AbstractCharacter.ElementType.FIRE, 0);
        this.addWeakness(AbstractCharacter.ElementType.PHYSICAL);
        this.addWeakness(AbstractCharacter.ElementType.FIRE);
        this.speedPriority = index;
    }
}
