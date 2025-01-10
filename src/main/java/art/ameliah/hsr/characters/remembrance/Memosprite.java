package art.ameliah.hsr.characters.remembrance;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysBasicGoal;

public abstract class Memosprite<C extends Memosprite<C>> extends AbstractCharacter<C> {
    public Memosprite(String name, int baseHP, int baseAtk, int baseDef, int baseSpeed, int level, ElementType elementType, float maxEnergy, int tauntValue, Path path) {
        super(name, baseHP, baseAtk, baseDef, baseSpeed, level, elementType, maxEnergy, tauntValue, path);

        this.registerGoal(0, new AlwaysBasicGoal<>((C)this));
    }

    protected abstract void memoSkill();
    public abstract AbstractCharacter<?> getMaster();

    @Override
    protected final void useBasic() {
        this.memoSkill();
    }

    @Override
    protected final void useSkill() {
    }

    @Override
    protected final void useUltimate() {
    }
}
