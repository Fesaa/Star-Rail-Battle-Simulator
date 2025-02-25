package art.ameliah.hsr.powers.dot;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.TurnStartEvent;
import art.ameliah.hsr.powers.TempPower;

public class EnemyBurn extends TempPower {

    protected final AbstractEnemy source;
    protected final float dmg;

    public EnemyBurn(AbstractEnemy source, float dmg, int turns) {
        super(turns);

        this.source = source;
        this.dmg = dmg;
    }

    @Subscribe
    public void onTurnStart(TurnStartEvent event) {
        this.source.doAttack(da -> da.logic((AbstractCharacter<?>) this.getOwner(), (e, al) -> al.hit(e, this.dmg)));
    }
}
