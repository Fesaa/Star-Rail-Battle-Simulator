package art.ameliah.hsr.battleLogic.log;

public interface Loggable {

    default String prefix() {
        return "";
    }

    String asString();

    void handle(Logger logger);

}
