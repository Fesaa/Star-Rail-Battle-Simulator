package art.ameliah.hsr.powers.dot;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.TempPower;

public class EnemyBurn extends TempPower {

    protected final AbstractEnemy source;
    protected final float dmg;

    public EnemyBurn(AbstractEnemy source, float dmg, int turns) {
        super(turns);

        this.source = source;
        this.dmg = dmg;
    }

    @Override
    public void onTurnStart() {
        this.source.startAttack()
                .hit((AbstractCharacter<?>) this.getOwner(), this.dmg)
                .execute();
    }
}
