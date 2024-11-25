package art.ameliah.hsr.battleLogic.log.lines.metrics;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FinalDmgMetrics implements Loggable {

    public final int totalPlayerDmg;
    public final float actionValueUsed;
    public final Map<BattleParticipant, Float> totalDamageDealt;

    public FinalDmgMetrics(IBattle battle) {
        this.totalPlayerDmg = battle.getTotalPlayerDmg();
        this.actionValueUsed = battle.getActionValueUsed();
        this.totalDamageDealt = new HashMap<>(battle.getDamageContributionMap());
        battle.getPlayers().forEach(p -> this.totalDamageDealt.putIfAbsent(p, 0f));
    }

    @Override
    public String asString() {
        String log = totalDamageDealt.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(entry -> {
                    float percent = entry.getValue() / totalPlayerDmg * 100;
                    return String.format("%s: %.3f DPAV (%.3f%%)", entry.getKey().getName(), entry.getValue() / actionValueUsed, percent);
                })
                .collect(Collectors.joining(" | "));
        return String.format("Damage Contribution: %s | Total Damage: %,d", log, totalPlayerDmg);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
