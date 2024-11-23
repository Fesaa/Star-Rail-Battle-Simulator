package art.ameliah.hsr.battleLogic.combat;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Hit {

    @Nullable
    AbstractCharacter<?> getSource();
    AbstractEnemy getTarget();
    List<DamageType> getTypes();
    float finalDmg();
    float finalToughnessReduction();

}
