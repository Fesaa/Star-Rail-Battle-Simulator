package art.ameliah.hsr.enemies;

import art.ameliah.hsr.characters.ElementType;

public class AllWeakPassiveEnemy extends PassiveEnemy {

    public AllWeakPassiveEnemy(int index) {
        this(index, false);
    }

    public AllWeakPassiveEnemy(int index, boolean middle) {
        // The middle ensure the HighestHpEnemy goal targets the middle enemy
        super("AllWeakPassiveEnemy" + index, EnemyType.Elite, 301193 + (middle ? 1 : 0), 718, 1150, 150, 100);
        setRes(ElementType.FIRE, 0);
        setRes(ElementType.WIND, 0);
        setRes(ElementType.IMAGINARY, 0);
        setRes(ElementType.LIGHTNING, 0);
        setRes(ElementType.PHYSICAL, 0);
        setRes(ElementType.QUANTUM, 0);
        setRes(ElementType.ICE, 0);
        this.addWeakness(ElementType.FIRE);
        this.addWeakness(ElementType.WIND);
        this.addWeakness(ElementType.IMAGINARY);
        this.addWeakness(ElementType.LIGHTNING);
        this.addWeakness(ElementType.PHYSICAL);
        this.addWeakness(ElementType.QUANTUM);
        this.addWeakness(ElementType.ICE);
        this.speedPriority = index;
    }
}
