package art.ameliah.hsr;

import art.ameliah.hsr.battleLogic.Battle;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.log.DefaultLogger;
import art.ameliah.hsr.battleLogic.log.LogSupplier;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.battleLogic.log.VoidLogger;
import art.ameliah.hsr.builder.BattleConfig;
import art.ameliah.hsr.builder.ConfigLoader;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.game.amphoreus.NoontideGryphon;
import art.ameliah.hsr.enemies.game.stellaronhunters.Kafka;
import art.ameliah.hsr.metrics.DmgContributionMetric;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
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
        float max = list.getFirst().getValue().total();
        int maxKeyLength = list.stream()
                .mapToInt(e -> e.getKey().length())
                .max()
                .orElse(0);

        for (var e : list) {
            var perMax = e.getValue().total() / max;
            var perMin = e.getValue().total() / min;

            String paddedKey = String.format("%-" + maxKeyLength + "s", e.getKey());
            System.out.printf("%s: %,.3f > %,.3f %% > %,.3f %% \n", paddedKey, e.getValue().total(), perMax, perMin);
        }
    }

    private static void BattleConsumer(BattleConfig cfg) {
        Battle battle = getTestBattle();
        battle.setPlayerTeam(cfg.characters);
        battle.setLogger(getLogger(cfg.getKey()));
        System.out.println("Running: " + cfg.getKey());
        battle.Start(250);
        DmgContributionMetric metric = battle.getMetricRegistry().getMetric("battle-dmg-contribution");
        metrics.put(cfg.getKey(), metric);
    }

    private static LogSupplier getLogger(String key) {
        String path = "export/castorice-sim";
        File dir = new File(path);
        if (!dir.exists() && !dir.mkdir()) {
            throw new RuntimeException("Couldn't create directory " + path);
        }

        PrintStream printStream;
        try {
            printStream = new PrintStream(new FileOutputStream(path + "/" + key + ".log"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return battle -> new DefaultLogger(battle, printStream);
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
