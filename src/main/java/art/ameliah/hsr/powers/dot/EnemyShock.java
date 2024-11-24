package art.ameliah.hsr.powers.dot;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.TempPower;
import lombok.Getter;

public class EnemyShock extends TempPower {

    public static final String NAME = "EnemyShock";

    protected final AbstractEnemy source;
    @Getter
    protected final float dmg;

    public EnemyShock(AbstractEnemy source, float dmg, int turns, int maxStack) {
        super(turns, NAME);
        this.type = PowerType.DEBUFF;
        this.maxStacks = maxStack;
        this.dmg = dmg;
        this.source = source;
    }

    @Override
    public void onTurnStart() {
        this.source.startAttack()
                .hit((AbstractCharacter<?>) this.getOwner(), this.dmg)
                .execute();
    }
}
