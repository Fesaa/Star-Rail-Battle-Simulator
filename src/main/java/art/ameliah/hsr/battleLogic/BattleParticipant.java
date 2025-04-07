package art.ameliah.hsr.battleLogic;

public interface BattleParticipant extends Comparable<BattleParticipant> {

    IBattle getBattle();

    default String getName() {
        return getClass().getSimpleName();
    }

    default boolean inBattle() {
        return getBattle() != null && getBattle().inCombat();
    }

    @Override
    default int compareTo(BattleParticipant other) {
        return getName().compareTo(other.getName());
    }

}
