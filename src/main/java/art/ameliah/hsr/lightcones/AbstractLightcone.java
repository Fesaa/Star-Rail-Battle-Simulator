package art.ameliah.hsr.lightcones;

import art.ameliah.hsr.battleLogic.BattleEvents;
import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.enemies.AbstractEnemy;

import java.util.Collection;
import java.util.List;

public abstract class AbstractLightcone implements BattleEvents, BattleParticipant {

    public final int baseHP;
    public final int baseAtk;
    public final int baseDef;
    public final AbstractCharacter<?> owner;

    public AbstractLightcone(int baseHP, int baseAtk, int baseDef, AbstractCharacter<?> owner) {
        this.baseHP = baseHP;
        this.baseAtk = baseAtk;
        this.baseDef = baseDef;
        this.owner = owner;
    }

    @Override
    public IBattle getBattle() {
        return this.owner.getBattle();
    }

    public void onEquip() {

    }

    public void onSpecificTrigger(AbstractCharacter<?> character, AbstractEnemy enemy) {

    }

    public final void useOnAlly(AbstractCharacter<?> target, MoveType action) {
        this.useOnAlly(List.of(target), action);
    }

    public void useOnAlly(Collection<AbstractCharacter<?>> targets, MoveType action) {}

    public String toString() {
        return this.getClass().getSimpleName();
    }

}
