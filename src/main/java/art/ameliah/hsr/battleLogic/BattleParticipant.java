package art.ameliah.hsr.battleLogic;

public interface BattleParticipant {

    IBattle getBattle();

    default boolean inBattle() {
        return getBattle() != null && getBattle().inCombat();
    }

}
