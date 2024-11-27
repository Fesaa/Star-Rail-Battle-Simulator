package art.ameliah.hsr.powers.dot;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.TempPower;

public class EnemyFrozen extends TempPower {

    public static final String NAME = "EnemyFrozen";

    private final AbstractEnemy source;
    private final int dmg;

    public EnemyFrozen(AbstractEnemy source, int turns, int dmg) {
        super(turns, NAME);

        this.type = PowerType.DEBUFF;
        this.source = source;
        this.dmg = dmg;
    }

    @Override
    public void onTurnStart() {
        this.source.doAttack(da -> da.logic((AbstractCharacter<?>) this.getOwner(), (e, al) -> al.hit(e, this.dmg)));
    }
}
