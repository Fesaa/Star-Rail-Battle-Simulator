package art.ameliah.hsr.metrics;

public abstract class AbstractMetric implements Metric {

    private final String key;
    private final String desc;

    public AbstractMetric(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    @Override
    public String getDescription() {
        if (this.desc == null || this.desc.isEmpty()) {
            return this.getName();
        }
        return this.desc;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
