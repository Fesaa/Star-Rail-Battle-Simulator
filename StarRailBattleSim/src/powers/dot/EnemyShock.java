package powers.dot;

import characters.AbstractCharacter;
import enemies.AbstractEnemy;
import powers.TempPower;

public class EnemyShock extends TempPower {

    public static String NAME = "EnemyShock";

    protected final AbstractEnemy source;
    protected final float dmg;

    public EnemyShock(AbstractEnemy source, float dmg, int turns, int maxStack) {
        super(turns, NAME);
        this.maxStacks = maxStack;
        this.dmg = dmg;
        this.source = source;
    }

    public float getDmg() {
        return dmg;
    }

    @Override
    public void onTurnStart() {
        getBattle().getHelper().attackCharacter(this.source, (AbstractCharacter<?>) this.owner, 0, this.dmg);
    }
}
