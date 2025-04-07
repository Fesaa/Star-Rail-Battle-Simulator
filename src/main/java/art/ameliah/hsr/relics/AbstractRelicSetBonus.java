package art.ameliah.hsr.relics;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.MoveType;

import java.util.Collection;
import java.util.List;

public abstract class AbstractRelicSetBonus implements BattleParticipant {
    public final AbstractCharacter<?> owner;
    protected final boolean isFullSet;

    public AbstractRelicSetBonus(AbstractCharacter<?> owner, boolean fullSet) {
        this.owner = owner;
        this.isFullSet = fullSet;
    }

    public AbstractRelicSetBonus(AbstractCharacter<?> owner) {
        this(owner, true);
    }

    @Override
    public IBattle getBattle() {
        return this.owner.getBattle();
    }

    public void onEquip() {

    }

    public final void useOnAlly(AbstractCharacter<?> target, MoveType action) {
        this.useOnAlly(List.of(target), action);
    }

    public void useOnAlly(Collection<AbstractCharacter<?>> targets, MoveType action) {
    }

    public boolean isFullSet() {
        return isFullSet;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

}
