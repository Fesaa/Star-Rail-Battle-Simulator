package art.ameliah.hsr;

import art.ameliah.hsr.battleLogic.Battle;
import art.ameliah.hsr.battleLogic.BattleParticipant;
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
import lombok.extern.log4j.Log4j2;

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
import java.util.function.Function;
import java.util.stream.Stream;

@Log4j2
public class CastoriceSim {

    private static final Map<String, DmgContributionMetric> metrics = new HashMap<>();

    public static void run() {
        //var battles = ConfigLoader.loadTeams("castorice_sims");
        var battles = ConfigLoader.loadTeams("hyacine");
        battles.forEach(CastoriceSim::BattleConsumer);

        System.out.println();
        System.out.println();
        System.out.println("==============================");

        List<Map.Entry<String, DmgContributionMetric>> list = metrics.entrySet().stream()
                .sorted(Comparator.comparingDouble(e ->
                        e.getValue().DPAV().values().stream().mapToDouble(Float::floatValue).sum()))
                .toList()
                .reversed();

        double min = score(list.getLast());
        double max = score(list.getFirst());
        int maxKeyLength = list.stream()
                .mapToInt(e -> e.getKey().length())
                .max()
                .orElse(0);

        for (var e : list) {
            var score = score(e);
            var perMax = score / max * 100;
            var perMin = score / min * 100;

            String paddedKey = String.format("%-" + maxKeyLength + "s", e.getKey());
            System.out.printf("%s: %,.3f > %,.3f %% > %,.3f %% \n", paddedKey, score, perMax, perMin);
        }
    }

    private static double score(Map.Entry<String, DmgContributionMetric> e) {
        return e.getValue().DPAV().values().stream().mapToDouble(Float::floatValue).sum();
    }

    private static void BattleConsumer(BattleConfig cfg) {
        Battle battle = getTestBattle();
        battle.setPlayerTeam(cfg.characters);
        battle.setLogger(getLogger(cfg.getKey()));
        log.info("Running {}", cfg.getKey());
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
            printStream = new PrintStream(new FileOutputStream(path + "/" + key.replace("/", "-") + ".log"));
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
