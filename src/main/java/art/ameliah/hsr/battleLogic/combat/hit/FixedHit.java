package art.ameliah.hsr.battleLogic.combat.hit;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@RequiredArgsConstructor
public class FixedHit implements HitHolder, Hit {

    @Getter
    @Nullable
    private final AttackLogic attackLogic;
    private final BattleParticipant source;
    private final AbstractEnemy target;
    private final float dmg;

    @Override
    public List<Hit> getHits() {
        return List.of(this);
    }

    @Override
    public BattleParticipant getSource() {
        return this.source;
    }

    @Override
    public AbstractEnemy getTarget() {
        return this.target;
    }

    @Override
    public List<DamageType> getTypes() {
        return List.of(DamageType.TRUE_DAMAGE);
    }

    @Override
    public ElementType getElementType() {
        return null;
    }

    @Override
    public float finalDmg() {
        return this.dmg;
    }

    @Override
    public float finalToughnessReduction() {
        return 0;
    }
}
