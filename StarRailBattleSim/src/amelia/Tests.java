package amelia;

import battleLogic.Battle;
import battleLogic.IBattle;
import battleLogic.log.VoidLogger;
import battleLogic.log.lines.metrics.BattleMetrics;
import battleLogic.log.lines.metrics.FinalDmgMetrics;
import battleLogic.log.lines.metrics.PostCombatPlayerMetrics;
import battleLogic.log.lines.metrics.StatType;
import characters.AbstractCharacter;
import enemies.AbstractEnemy;
import enemies.FireWindImgLightningWeakEnemy;
import lightcones.harmony.ButTheBattleIsntOver;
import lightcones.harmony.FlowingNightglow;
import lightcones.harmony.ForTomorrowsJourney;
import lightcones.harmony.MemoriesOfThePast;
import lightcones.harmony.PoisedToBloom;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static amelia.FeixiaoTeams.*;

public class Tests {

    // ANSI escape codes for colors
    public static final String RESET = "\033[0m";  // Reset to default color
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";

    private static final Pattern ANSI_ESCAPE = Pattern.compile("\033\\[[;\\d]*m");

    static final List<BattleResult> results = new ArrayList<>();
    static final List<List<AbstractCharacter<?>>> teams  = new ArrayList<>();

    static {
        teams.add(FeixiaoMarch(
                () -> myRobin(FlowingNightglow::new),
                FeixiaoTeams::myGallagher
        ));
        teams.add(FeixiaoMarch(
                () -> myRobin(ForTomorrowsJourney::new),
                FeixiaoTeams::myGallagher
        ));
        teams.add(FeixiaoMarch(
                () -> myRobin(PoisedToBloom::new),
                FeixiaoTeams::myGallagher
        ));

        teams.add(FeixiaoMarch(
                () -> myRobin(PoisedToBloom::new),
                () -> myBroyna(ButTheBattleIsntOver::new)
        ));
        teams.add(FeixiaoMarch(
                () -> myRobin(PoisedToBloom::new),
                () -> myBroyna(PoisedToBloom::new)
        ));
        teams.add(FeixiaoMarch(
                () -> myRobin(FlowingNightglow::new),
                () -> myBroyna(ButTheBattleIsntOver::new)
        ));
        teams.add(FeixiaoMarch(
                () -> myRobin(FlowingNightglow::new),
                () -> myBroyna(PoisedToBloom::new)
        ));
        teams.add(FeixiaoMarch(
                () -> myRobin(ForTomorrowsJourney::new),
                () -> myBroyna(ButTheBattleIsntOver::new)
        ));
        teams.add(FeixiaoMarch(
                () -> myRobin(ForTomorrowsJourney::new),
                () -> myBroyna(PoisedToBloom::new)
        ));

        teams.add(FeixiaoMarch(
                () -> myRobin(PoisedToBloom::new),
                () -> myRuanMei(MemoriesOfThePast::new)
        ));
        teams.add(FeixiaoMarch(
                () -> myRobin(PoisedToBloom::new),
                () -> myRuanMei(PoisedToBloom::new)
        ));
        teams.add(FeixiaoMarch(
                () -> myRobin(FlowingNightglow::new),
                () -> myRuanMei(MemoriesOfThePast::new)
        ));
        teams.add(FeixiaoMarch(
                () -> myRobin(FlowingNightglow::new),
                () -> myRuanMei(PoisedToBloom::new)
        ));
        teams.add(FeixiaoMarch(
                () -> myRobin(ForTomorrowsJourney::new),
                () -> myRuanMei(MemoriesOfThePast::new)
        ));
        teams.add(FeixiaoMarch(
                () -> myRobin(ForTomorrowsJourney::new),
                () -> myRuanMei(PoisedToBloom::new)
        ));
    }

    public static void runTests() {
        setupBattle(teams.get(0)).Start(550);
        teams.parallelStream()
                .skip(1)
                .forEach(team -> setupBattle(team).Start(550));

        compareBattleResults();
    }

    private static IBattle setupBattle(List<AbstractCharacter<?>> team) {
        BattleResult result = new BattleResult();
        result.team = team;
        IBattle battle = new Battle(b -> new VoidLogger(b) {final

            @Override
            public void handle(FinalDmgMetrics finalDmgMetrics) {
                result.finalDmgMetrics = finalDmgMetrics;
                synchronized (Tests.results) {
                    Tests.results.add(result);
                }
            }

            @Override
            public void handle(BattleMetrics battleMetrics) {
                result.battleMetrics = battleMetrics;
            }

            @Override
            public void handle(PostCombatPlayerMetrics postCombatPlayerMetrics) {
                result.postCombatPlayerMetrics = postCombatPlayerMetrics;
            }
        });
        List<AbstractEnemy> enemyTeam = new ArrayList<>();
        enemyTeam.add(new FireWindImgLightningWeakEnemy(0, 0));
        battle.setEnemyTeam(enemyTeam);
        battle.setPlayerTeam(team);

        return battle;
    }

    private static void compareBattleResults() {
        int baseTotalPlayerDmg = -1;
        float baseFinalDPAV = -1;

        List<String> headers = new ArrayList<>();
        headers.add("Team");
        headers.add("Total Player Dmg");
        headers.add("Final DPAV");
        List<List<String>> columns = new ArrayList<>();
        for (BattleResult result : results) {
            columns.add(formatResult(result, baseTotalPlayerDmg, baseFinalDPAV));

            if (baseTotalPlayerDmg == -1) {
                baseTotalPlayerDmg = result.finalDmgMetrics.totalPlayerDmg;
            }
            if (baseFinalDPAV == -1) {
                baseFinalDPAV = result.battleMetrics.finalDPAV;
            }
        }

        printTable(System.out, headers, columns);
    }

    private static List<String> formatResult(BattleResult result, int lastTotalPlayerDmg, float lastFinalDPAV) {
        int totalPlayerDmg = result.finalDmgMetrics.totalPlayerDmg;
        float finalDPAV = result.battleMetrics.finalDPAV;

        List<String> columns = new ArrayList<>();

        String ID = result.team.stream().map(c -> String.format("%s(%s)", c.name, c.lightcone.toString())).collect(Collectors.joining(", "));
        columns.add(ID);

        String totalPlayerDmgStr = "" + totalPlayerDmg;
        if (lastTotalPlayerDmg != -1) {
            float per = calculatePercentageChange(lastTotalPlayerDmg, totalPlayerDmg);
            totalPlayerDmgStr += String.format(" %s(%+.2f%%)%s", per > 0 ? GREEN : RED, per, RESET);
        }
        columns.add(totalPlayerDmgStr);

        String finalDPAVStr = "" + finalDPAV;
        if (lastFinalDPAV != -1) {
            float per = calculatePercentageChange(lastFinalDPAV, finalDPAV);
            finalDPAVStr += String.format(" %s(%+.2f%%)%s", per > 0 ? GREEN : RED, per, RESET);
        }
        columns.add(finalDPAVStr);

        return columns;
    }


    private static float calculatePercentageChange(float oldValue, float newValue) {
        if (oldValue == 0) {
            return newValue == 0 ? 0 : 100;
        }
        return ((newValue - oldValue) / oldValue) * 100;
    }

    public static void printTable(PrintStream out, List<String> headers, List<List<String>> rows) {
        if (headers == null || rows == null || headers.isEmpty() || rows.isEmpty()) {
            throw new IllegalArgumentException("Headers and rows must not be null and must have at least one element.");
        }

        // Determine the maximum width for each column
        int[] columnWidths = new int[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            columnWidths[i] = getStrippedLength(headers.get(i));
            for (List<String> row : rows) {
                if (i < row.size()) {
                    columnWidths[i] = Math.max(columnWidths[i], getStrippedLength(row.get(i)));
                }
            }
        }

        // Print headers
        for (int i = 0; i < headers.size(); i++) {
            out.print(padRight(headers.get(i), columnWidths[i]) + " ");
        }
        out.println();

        // Print rows
        for (List<String> row : rows) {
            for (int i = 0; i < headers.size(); i++) {
                String value = i < row.size() ? row.get(i) : "";
                out.print(padRight(value, columnWidths[i]) + " ");
            }
            out.println();
        }
    }

    private static int getStrippedLength(String s) {
        return ANSI_ESCAPE.matcher(s).replaceAll("").length();
    }

    private static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

}
