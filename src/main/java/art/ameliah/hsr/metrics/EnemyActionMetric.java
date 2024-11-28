package art.ameliah.hsr.metrics;

import art.ameliah.hsr.enemies.EnemyAttackType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnemyActionMetric extends AbstractMetric {

    private final List<EnemyAttackType> actions = new ArrayList<>();
    private final Map<EnemyAttackType, Integer> frequency = new HashMap<>();

    public EnemyActionMetric(String key, String desc) {
        super(key, desc);
    }

    public void record(EnemyAttackType type) {
        this.actions.add(type);
        this.frequency.put(type, frequency.getOrDefault(type, 0) + 1);
    }

    @Override
    public String representation() {
        StringBuilder sb = new StringBuilder();

        sb.append("Total #actions").append(": ").append(this.actions.size()).append(System.lineSeparator());

        for (var e : this.frequency.entrySet()) {
            sb.append(e.getKey()).append(": ").append(e.getValue()).append(System.lineSeparator());
        }

        sb.append("Move Order").append(": ").append(this.actions);
        return sb.toString();
    }
}
