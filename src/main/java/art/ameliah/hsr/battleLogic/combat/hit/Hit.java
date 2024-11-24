package art.ameliah.hsr.battleLogic.combat.hit;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Hit {

    BattleParticipant getSource();

    AbstractEnemy getTarget();

    List<DamageType> getTypes();

    @Nullable
    ElementType getElementType();

    float finalDmg();

    float finalToughnessReduction();

}
