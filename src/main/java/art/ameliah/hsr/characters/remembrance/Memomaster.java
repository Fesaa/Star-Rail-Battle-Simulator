package art.ameliah.hsr.characters.remembrance;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.Path;
import org.jetbrains.annotations.Nullable;

public abstract class Memomaster<C extends Memomaster<C>> extends AbstractCharacter<C> {
    public Memomaster(String name, int baseHP, int baseAtk, int baseDef, int baseSpeed, int level, ElementType elementType, float maxEnergy, int tauntValue, Path path) {
        super(name, baseHP, baseAtk, baseDef, baseSpeed, level, elementType, maxEnergy, tauntValue, path);
    }

    protected abstract void summonMemo();

    /**
     * Should return null if the memosprite is currently not on the field
     *
     * @return the memosprite
     */
    @Nullable
    public abstract Memosprite<?> getMemo();
}
