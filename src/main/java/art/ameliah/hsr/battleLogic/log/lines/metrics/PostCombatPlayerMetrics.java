package art.ameliah.hsr.battleLogic.log.lines.metrics;

import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.MoveType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostCombatPlayerMetrics implements Loggable {

    public final AbstractCharacter<?> player;
    public final Map<StatType, Float> finalStats = new HashMap<>();
    public final Map<String, Boolean> relicSets = new HashMap<>();
    public final int turnsTaken;
    public final int basics;
    public final int skills;
    public final int ultimates;
    public final List<MoveType> rotation;
    public final Map<String, String> characterSpecificMetrics;
    public final boolean lessMetrics;

    public PostCombatPlayerMetrics(AbstractCharacter<?> player, boolean lessMetrics) {
        this.player = player;

        finalStats.put(StatType.ATK, player.getFinalAttack());
        finalStats.put(StatType.DEF, player.getFinalDefense());
        finalStats.put(StatType.HP, player.getFinalHP());
        finalStats.put(StatType.SPD, player.getFinalSpeed());
        finalStats.put(StatType.DMG, player.getTotalSameElementDamageBonus());
        finalStats.put(StatType.CRIT, player.getTotalCritChance());
        finalStats.put(StatType.CRITDMG, player.getTotalCritDamage());
        finalStats.put(StatType.BREAK, player.getTotalBreakEffect());
        finalStats.put(StatType.EHR, player.getTotalEHR());
        finalStats.put(StatType.ERR, player.getTotalERR());
        finalStats.put(StatType.RES, player.getTotalEffectRes());
        player.relicSetBonus.forEach(r -> relicSets.put(r.toString(), r.isFullSet()));

        this.turnsTaken = player.numTurnsMetric;
        this.basics = player.numBasicsMetric;
        this.skills = player.numSkillsMetric;
        this.ultimates = player.numUltsMetric;
        this.rotation = player.moveHistory;
        this.characterSpecificMetrics = new HashMap<>(player.getCharacterSpecificMetricMap());
        this.lessMetrics = lessMetrics;
    }

    @Override
    public String asString() {
        String statsString;
        String gearString = String.format("Metrics for %s \nLightcone: %s \nRelic Set Bonuses: ", player.name, player.lightcone);
        gearString += player.relicSetBonus;
        statsString = gearString;
        if (!lessMetrics) {
            statsString += String.format("\nAfter combat stats \nAtk: %.3f \nDef: %.3f \nHP: %.3f \nSpeed: %.3f \nSame Element Damage Bonus: %.3f \nCrit Chance: %.3f%% \nCrit Damage: %.3f%% \nBreak Effect: %.3f%%",
                    finalStats.get(StatType.ATK), finalStats.get(StatType.DEF), finalStats.get(StatType.HP), finalStats.get(StatType.SPD), finalStats.get(StatType.DMG), finalStats.get(StatType.CRIT), finalStats.get(StatType.CRITDMG), finalStats.get(StatType.BREAK));
        }
        StringBuilder metrics = new StringBuilder(statsString + "\nCombat Metrics");
        if (!lessMetrics) {
            metrics.append(String.format("\n Rotation: %s", rotation));
        }
        for (String metric : player.getOrderedCharacterSpecificMetricsKeys()) {
            metrics.append("\n").append(metric).append(": ").append(characterSpecificMetrics.get(metric));
        }
        return metrics + "\n";
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
