package art.ameliah.hsr.metrics;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MetricRegistry {

    private final Object owner;
    private final Map<String, Metric> metrics = new HashMap<>();

    public MetricRegistry(Object owner) {
        this.owner = owner;
    }

    public Collection<Metric> AllMetrics() {
        return this.metrics.values();
    }

    @SuppressWarnings("unchecked")
    public <T extends Metric> T getMetric(String key) {
        return (T) this.metrics.getOrDefault(key, null);
    }

    /**
     * Register a new metric
     *
     * @param metric the metric
     * @param <T>    type
     * @return the metric
     */
    public <T extends Metric> T register(T metric) {
        if (this.metrics.containsKey(metric.getKey())) {
            System.out.printf("[WARN] %s is replacing a metric by sharing the same key (%s)\n",
                    metric.getClass().getSimpleName(), metric.getKey());
        }
        this.metrics.put(metric.getKey(), metric);
        return metric;
    }

    /**
     * Construct, and register a new metric of the passed class, will throw if no constructor with key is found
     *
     * @param key   key to sue for the new metric, description will be empty
     * @param clazz the class of the metric
     * @param <T>   type
     * @return the new metric
     */
    public <T extends Metric> T register(String key, Class<T> clazz) {
        return this.register(key, "", clazz);
    }

    /**
     * Construct, and register a new metric of the passed class, will throw if no constructor with key is found
     *
     * @param key   key to use for the new metric
     * @param desc  the description to use for the new metric
     * @param clazz the class of the metric
     * @param <T>   type
     * @return the new metric
     */
    public <T extends Metric> T register(String key, String desc, Class<T> clazz) {
        try {
            return this.register(clazz.getConstructor(String.class, String.class).newInstance(key, desc));
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public String representation() {
        StringBuilder sb = new StringBuilder();

        //sb.append("Metrics for: ").append(owner.getClass().getSimpleName());
        for (Metric metric : this.metrics.values()) {
            if (metric.needHeader()) {
                sb.append(metric.getName()).append("\n");
            }
            sb.append(metric.representation()).append("\n");
        }

        return sb.toString();
    }

}
