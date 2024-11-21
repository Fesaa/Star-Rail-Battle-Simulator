package art.ameliah.hsr.relics;

import art.ameliah.hsr.battleLogic.BattleEvents;
import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.characters.AbstractCharacter;

public abstract class AbstractRelicSetBonus implements BattleEvents,BattleParticipant {
    public final AbstractCharacter<?> owner;
    protected final boolean isFullSet;

    public AbstractRelicSetBonus(AbstractCharacter<?> owner, boolean fullSet) {
        this.owner = owner;
        this.isFullSet = fullSet;
    }

    @Override
    public IBattle getBattle() {
        return this.owner.getBattle();
    }

    public AbstractRelicSetBonus(AbstractCharacter<?> owner) {
        this(owner, true);
    }

    public void onEquip() {

    }

    public boolean isFullSet() {
        return isFullSet;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

}
