package art.ameliah.hsr.characters;

import art.ameliah.hsr.battleLogic.AbstractSummon;

import java.util.List;

public abstract class AbstractSummoner<C extends AbstractSummoner<C>> extends AbstractCharacter<C> {
    public AbstractSummoner(String name, int baseHP, int baseAtk, int baseDef, int baseSpeed, int level, ElementType elementType, float maxEnergy, int tauntValue, Path path) {
        super(name, baseHP, baseAtk, baseDef, baseSpeed, level, elementType, maxEnergy, tauntValue, path);
    }

    public abstract List<AbstractSummon<C>> getSummons();
}
