package art.ameliah.hsr.battleLogic.log;

public interface Loggable {

    String asString();

    void handle(Logger logger);

}
