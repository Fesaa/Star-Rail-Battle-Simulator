package art.ameliah.hsr;

import art.ameliah.hsr.battleLogic.Battle;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.log.VoidLogger;
import art.ameliah.hsr.builder.BattleConfig;
import art.ameliah.hsr.builder.ConfigLoader;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.game.amphoreus.NoontideGryphon;
import art.ameliah.hsr.enemies.game.stellaronhunters.Kafka;
import art.ameliah.hsr.metrics.DmgContributionMetric;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CastoriceSim {

    private static final Map<String, DmgContributionMetric> metrics = new HashMap<>();

    public static void run() {
        var battles = ConfigLoader.loadTeams("castorice_sims");
        battles.forEach(CastoriceSim::BattleConsumer);

        System.out.println();
        System.out.println();
        System.out.println("==============================");

        List<Map.Entry<String, DmgContributionMetric>> list = metrics.entrySet().stream()
                .sorted(Comparator.comparingDouble(e -> e.getValue().total()))
                .toList()
                .reversed();

        float min = list.getLast().getValue().total();
        float prev = 0;
        int maxKeyLength = list.stream()
                .mapToInt(e -> e.getKey().length())
                .max()
                .orElse(0);

        for (var e : list) {
            if (prev == 0) {
                prev = e.getValue().total();
            }
            var per = e.getValue().total() / prev;
            var perMin = e.getValue().total() / min;

            String paddedKey = String.format("%-" + maxKeyLength + "s", e.getKey());
            System.out.printf("%s: %,.3f > %,.3f %% > %,.3f %% \n", paddedKey, e.getValue().total(), per, perMin);

            prev = e.getValue().total();
        }
    }

    private static void BattleConsumer(BattleConfig cfg) {
        Battle battle = getTestBattle();
        battle.setPlayerTeam(cfg.characters);
        battle.setLogger(VoidLogger::new);
        battle.Start(550);
        DmgContributionMetric metric = battle.getMetricRegistry().getMetric("battle-dmg-contribution");
        metrics.put(cfg.getKey(), metric);
    }

    private static Battle getTestBattle() {
        Battle battle = new Battle();
        List<AbstractEnemy> enemyTeam = new ArrayList<>();
        enemyTeam.add(new NoontideGryphon(1_000_000));
        enemyTeam.add(new Kafka(2_000_000));
        enemyTeam.add(new NoontideGryphon(1_000_000));
        battle.setEnemyTeam(enemyTeam);
        return battle;
    }

}
