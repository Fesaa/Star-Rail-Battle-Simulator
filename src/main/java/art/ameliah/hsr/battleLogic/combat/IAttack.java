package art.ameliah.hsr.battleLogic.combat;

public interface IAttack {
    void execute(boolean forceFirst);

    default void execute() {
        this.execute(false);
    }
}
