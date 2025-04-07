package art.ameliah.hsr.powers.dot;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.TurnStartEvent;
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

    @Subscribe
    public void onTurnStart(TurnStartEvent event) {
        this.source.doAttack(da -> da.logic((AbstractCharacter<?>) this.getOwner(), (e, al) -> al.hit(e, this.dmg)));
    }
}
