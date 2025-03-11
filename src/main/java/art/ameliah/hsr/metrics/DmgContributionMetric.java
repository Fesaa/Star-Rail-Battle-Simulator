package art.ameliah.hsr.metrics;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.combat.result.HitResult;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DmgContributionMetric extends AbstractMetric {

    private final Map<BattleParticipant, Float> map = new ConcurrentHashMap<>();
    private final Map<BattleParticipant, TreeMap<DamageType, Float>> dmgPerType = new ConcurrentHashMap<>();
    private final Map<BattleParticipant, Float> overFlowMap = new ConcurrentHashMap<>();
    private final IBattle battle;

    public DmgContributionMetric(IBattle battle, String key, String desc) {
        super(key, desc);
        this.battle = battle;
    }

    public void record(BattleParticipant participant, HitResult res) {
        this.map.put(participant, this.map.getOrDefault(participant, 0.0f) + res.getHit().finalDmg());

        var damageTypeFloatMap = this.dmgPerType.computeIfAbsent(participant, _ -> {
            return new TreeMap<>(Comparator.comparing(DamageType::name));
        });
        for (var type : res.getHit().getTypes()) {
            damageTypeFloatMap.put(type, damageTypeFloatMap.getOrDefault(type, 0.0f) + res.getHit().finalDmg());
        }
        this.dmgPerType.put(participant, damageTypeFloatMap);

        float overflow = res.getHit().finalDmg() - res.getDmgDealt();
        this.overFlowMap.put(participant, this.overFlowMap.getOrDefault(participant, 0.0f) + overflow);
    }

    public float total() {
        return this.map.values().stream().reduce(0f, Float::sum);
    }

    @Override
    public String representation() {
        return String.format("Damage Contribution: %s \nOverflow damage %s\n%s",
                this.contribution(), this.overFlowMap, this.characterBreakDown());
    }

    public String overflow() {
        return overFlowMap.entrySet().stream()
                .collect(Collectors.groupingBy(entry -> entry.getKey().getName(),
                        Collectors.summingDouble(Map.Entry::getValue)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue(Comparator.reverseOrder()))
                .map(entry -> {
                    double totalDmg = map.entrySet().stream()
                            .filter(e -> e.getKey().getName().equals(entry.getKey()))
                            .mapToDouble(Map.Entry::getValue)
                            .sum();
                    double percent = entry.getValue() / totalDmg * 100;
                    return String.format("%s: %,.0f DMG (%.3f%%)", entry.getKey(), entry.getValue(), percent);
                })
                .collect(Collectors.joining(" | "));
    }

    public String contribution() {
        return map.entrySet().stream()
                .collect(Collectors.groupingBy(entry -> entry.getKey().getName(),
                        Collectors.summingDouble(Map.Entry::getValue)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue(Comparator.reverseOrder()))
                .map(entry -> {
                    double totalBattleDmg = battle.getTotalPlayerDmg();
                    double percent = entry.getValue() / totalBattleDmg * 100;
                    double totalActionValueUsed = battle.getActionValueUsed();
                    return String.format("%s: %,.3f DPAV (%.3f%%)", entry.getKey(), entry.getValue() / totalActionValueUsed, percent);
                })
                .collect(Collectors.joining(" | "));
    }

    private String characterBreakDown() {
        return this.dmgPerType.entrySet().stream()
                .filter(e -> e.getKey() instanceof AbstractCharacter<?>)
                .collect(Collectors.groupingBy(entry -> entry.getKey().getName(),
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> {
                            TreeMap<DamageType, Float> merged = new TreeMap<>(a);
                            b.forEach((type, dmg) -> merged.merge(type, dmg, Float::sum));
                            return merged;
                        }, TreeMap::new)))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    String d = e.getValue().entrySet().stream()
                            .flatMap(entry -> entry.getValue().entrySet().stream()
                                    .map(inner -> String.format("%s: %,.3f", inner.getKey(), inner.getValue())))
                            .collect(Collectors.joining("\n\t"));
                    return String.format("%s:\n\t%s", e.getKey(), d);
                })
                .collect(Collectors.joining("\n"));
    }
}