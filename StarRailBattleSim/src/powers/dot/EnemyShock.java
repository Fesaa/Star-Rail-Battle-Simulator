package powers.dot;

import characters.AbstractCharacter;
import enemies.AbstractEnemy;
import powers.TempPower;

public class EnemyShock extends TempPower {

    private final AbstractEnemy source;
    private final float dmg;

    public EnemyShock(AbstractEnemy source, float dmg, int turns, int maxStack) {
        super(turns);
        this.maxStacks = maxStack;
        this.dmg = dmg;
        this.source = source;
    }

    @Override
    public void onTurnStart() {
        getBattle().getHelper().attackCharacter(this.source, (AbstractCharacter<?>) this.owner, 0, this.dmg);
    }
}
