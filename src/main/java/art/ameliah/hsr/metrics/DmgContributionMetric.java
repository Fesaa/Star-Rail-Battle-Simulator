package art.ameliah.hsr.metrics;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.combat.hit.Hit;
import art.ameliah.hsr.battleLogic.combat.result.HitResult;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DmgContributionMetric extends AbstractMetric{

    private final Map<BattleParticipant, Float> map = new ConcurrentHashMap<>();
    private final Map<BattleParticipant, Float> overFlowMap = new ConcurrentHashMap<>();
    private final IBattle battle;

    public DmgContributionMetric(IBattle battle, String key, String desc) {
        super(key, desc);
        this.battle = battle;
    }

    public void record(BattleParticipant participant, HitResult res) {
        this.map.put(participant, this.map.getOrDefault(participant, 0.0f) + res.getHit().finalDmg());

        float overflow = res.getHit().finalDmg() - res.getDmgDealt();
        this.overFlowMap.put(participant, this.overFlowMap.getOrDefault(participant, 0.0f) + overflow);
    }

    @Override
    public String representation() {
        String log = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(entry -> {
                    float percent = entry.getValue() / this.battle.getTotalPlayerDmg() * 100;
                    return String.format("%s: %,.3f DPAV (%.3f%%)", entry.getKey().getName(), entry.getValue() / this.battle.getActionValueUsed(), percent);
                })
                .collect(Collectors.joining(" | "));
        String overflow = overFlowMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(entry -> {
                    float percent = entry.getValue() / this.map.get(entry.getKey()) * 100;
                    return String.format("%s: %,.0f DMG (%.3f%%)", entry.getKey().getName(), entry.getValue(), percent);
                })
                .collect(Collectors.joining(" | "));
        return String.format("Total Damage: %,d\nDamage Contribution: %s \nOverflow damage %s", this.battle.getTotalPlayerDmg(), log, overflow);
    }
}
