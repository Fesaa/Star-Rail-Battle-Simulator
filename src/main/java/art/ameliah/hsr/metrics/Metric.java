package art.ameliah.hsr.metrics;

public interface Metric {

    String getKey();

    default String getName() {
        return this.getKey();
    }

    String getDescription();

    String representation();

    default boolean needHeader() {
        return false;
    }



}
