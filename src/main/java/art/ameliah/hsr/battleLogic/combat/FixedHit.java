package art.ameliah.hsr.battleLogic.combat;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FixedHit implements HitHolder,Hit {

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
        return List.of();
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
