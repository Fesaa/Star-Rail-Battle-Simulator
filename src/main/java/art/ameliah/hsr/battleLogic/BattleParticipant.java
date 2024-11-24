package art.ameliah.hsr.battleLogic;

public interface BattleParticipant {

    IBattle getBattle();

    default String getName() {
        return getClass().getSimpleName();
    }

    default boolean inBattle() {
        return getBattle() != null && getBattle().inCombat();
    }

}
