package art.ameliah.hsr.battleLogic.combat.result;

import art.ameliah.hsr.battleLogic.combat.hit.Hit;
import art.ameliah.hsr.enemies.AbstractEnemy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HitResult {

    private final Hit hit;
    private final AbstractEnemy enemy;
    private final float dmgDealt;
    private final float toughnessDealt;
    private final boolean weaknessBroken;
    private final boolean killed;

    public boolean success() {
        return this.dmgDealt > 0;
    }
}
