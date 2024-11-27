package art.ameliah.hsr.metrics;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class MetricRegistry {

    private final Object owner;
    private final HashMap<String, Metric> metrics = new HashMap<>();

    public MetricRegistry(Object owner) {
        this.owner = owner;
    }

    /**
     * Register a new metric
     * @param metric the metric
     * @return the metric
     * @param <T> type
     */
    public <T extends Metric> T register(T metric) {
        this.metrics.put(metric.getKey(), metric);
        return metric;
    }

    /**
     * Construct, and register a new metric of the passed class, will throw if no constructor with key is found
     * @param key key to sue for the new metric, description will be empty
     * @param clazz the class of the metric
     * @return the new metric
     * @param <T> type
     */
    public <T extends Metric> T register(String key, Class<T> clazz) {
        return this.register(key, "", clazz);
    }

    /**
     * Construct, and register a new metric of the passed class, will throw if no constructor with key is found
     * @param key key to use for the new metric
     * @param desc the description to use for the new metric
     * @param clazz the class of the metric
     * @return the new metric
     * @param <T> type
     */
    public <T extends Metric> T register(String key, String desc, Class<T> clazz) {
        try {
            return this.register(clazz.getConstructor(String.class, String.class).newInstance(key, desc));
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public String representation() {
        StringBuilder sb = new StringBuilder();

        //sb.append("Metrics for: ").append(owner.getClass().getSimpleName());
        for (Metric metric : metrics.values()) {
            if (metric.needHeader()) {
                sb.append(metric.getName()).append("\n");
            }
            sb.append(metric.representation()).append("\n");
        }

        return sb.toString();
    }

}