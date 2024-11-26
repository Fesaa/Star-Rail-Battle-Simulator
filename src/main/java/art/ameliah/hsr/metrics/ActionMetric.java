package art.ameliah.hsr.metrics;

import art.ameliah.hsr.characters.MoveType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionMetric extends AbstractMetric {

    private final List<MoveType> actions = new ArrayList<>();
    private final List<MoveType> turnActions = new ArrayList<>();
    private final Map<MoveType, Integer> frequency = new HashMap<>();

    public ActionMetric(String key, String desc) {
        super(key, desc);
    }

    public void record(MoveType type) {
        this.actions.addLast(type);
        this.frequency.put(type, this.frequency.getOrDefault(type, 0) + 1);

        if (type != MoveType.ULTIMATE) {
            this.turnActions.addLast(type);
        }
    }

    public int frequency(MoveType type) {
        return this.frequency.getOrDefault(type, 0);
    }

    public MoveType lookBackTurn(int i) {
        if (i >= this.turnActions.size()) {
            return null;
        }
        return this.turnActions.get(this.turnActions.size() - i);
    }

    public MoveType lastTurnAction() {
        return this.lookBackTurn(1);
    }

    public MoveType lookBack(int i) {
        if (i >= this.actions.size()) {
            return null;
        }
        return this.actions.get(this.actions.size() - i);
    }

    public MoveType last() {
        return this.lookBack(1);
    }

    @Override
    public String representation() {
        StringBuilder sb = new StringBuilder("Movements:\n");

        for (var e : this.frequency.entrySet()) {
            sb.append("\t").append(e.getKey()).append(" used").append(": ").append(e.getValue()).append("\n");
        }

        sb.append("Move Order").append(": ").append(this.actions);
        return sb.toString();
    }
}
