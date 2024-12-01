package art.ameliah.hsr.battleLogic;

import lombok.Getter;

import java.util.Random;

@Getter
public abstract class RngProvider {

    protected static final long seed = 154172837382L;

    public final Random enemyMoveRng = new Random(seed);
    public final Random enemyTargetRng = new Random(seed);
    public final Random critChanceRng = new Random(seed);
    public final Random getRandomEnemyRng = new Random(seed);
    public final Random procChanceRng = new Random(seed);
    public final Random gambleChanceRng = new Random(seed);
    public final Random qpqRng = new Random(seed);
    public final Random milkyWayRng = new Random(seed);
    public final Random weaveEffectRng = new Random(seed);
    public final Random aetherRng = new Random(seed);
    public final Random enemyEHRRng = new Random(seed);

    
    public long getSeed() {
        return seed;
    }


}
