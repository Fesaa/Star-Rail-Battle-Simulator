package art.ameliah.hsr.metrics;

import art.ameliah.hsr.characters.MoveType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionMetric extends AbstractMetric {

    private static final List<MoveType> turnActionTypes = List.of(MoveType.BASIC, MoveType.SKILL, MoveType.ENHANCED_BASIC, MoveType.ENHANCED_SKILL, MoveType.MEMOSPRITE_BASIC ,MoveType.MEMOSPRITE_SKILL);

    private final List<MoveType> actions = new ArrayList<>();
    private final List<MoveType> turnActions = new ArrayList<>();
    private final Map<MoveType, Integer> frequency = new HashMap<>();

    public ActionMetric(String key, String desc) {
        super(key, desc);
    }

    public void record(MoveType type) {
        this.actions.addLast(type);
        this.frequency.put(type, this.frequency.getOrDefault(type, 0) + 1);

        if (turnActions.contains(type)) {
            this.turnActions.addLast(type);
        }
    }

    public int frequency(MoveType type) {
        return this.frequency.getOrDefault(type, 0);
    }

    public boolean lastMove(MoveType move) {
        for (int i = this.actions.size() - 1; i > 0; i--) {
            MoveType previousMove = this.actions.get(i);
            if (turnActionTypes.contains(previousMove)) {
                return previousMove.equals(move);
            }
        }
        return false;
    }

    public boolean lastMoveBefore(MoveType move) {
        boolean skippedYet = false;
        for (int i = this.actions.size() - 1; i > 0; i--) {
            MoveType previousMove = this.actions.get(i);
            if (turnActionTypes.contains(previousMove)) {
                if (!skippedYet) {
                    skippedYet = true;
                } else {
                    return previousMove.equals(move);
                }
            }
        }
        return false;
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
