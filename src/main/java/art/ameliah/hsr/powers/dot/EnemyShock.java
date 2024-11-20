package art.ameliah.hsr.powers.dot;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.TempPower;

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
