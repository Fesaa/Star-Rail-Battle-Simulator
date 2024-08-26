package amelia;

import battleLogic.Battle;
import battleLogic.IBattle;
import battleLogic.log.MetricLogger;
import battleLogic.log.VoidLogger;
import battleLogic.log.lines.character.TotalDamage;
import battleLogic.log.lines.metrics.BattleMetrics;
import battleLogic.log.lines.metrics.FinalDmgMetrics;
import battleLogic.log.lines.metrics.PostCombatPlayerMetrics;
import battleLogic.log.lines.metrics.StatType;
import characters.AbstractCharacter;
import enemies.AbstractEnemy;
import enemies.FireWindImgLightningWeakEnemy;
import enemies.PhysFireWeakEnemy;
import lightcones.AbstractLightcone;
import lightcones.abundance.QuidProQuo;
import lightcones.harmony.ButTheBattleIsntOver;
import lightcones.harmony.FlowingNightglow;
import lightcones.harmony.ForTomorrowsJourney;
import lightcones.harmony.MemoriesOfThePast;
import lightcones.harmony.PoisedToBloom;
import lightcones.hunt.CruisingInTheStellarSea;
import lightcones.hunt.IVentureForthToHunt;
import lightcones.hunt.OnlySilenceRemains;
import lightcones.hunt.Swordplay;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
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


    static final List<AbstractCharacter<?>> baseTeam = FeixiaoMarch(
            () -> myRobin(FlowingNightglow::new),
            FeixiaoTeams::myGallagher
    );
    static final List<AbstractCharacter<?>> baseTeam2 = FeixiaoMarch(
            () -> myRobin(FlowingNightglow::new),
            () -> myBroyna(ButTheBattleIsntOver::new)
    );

    static final List<AbstractCharacter<?>> baseTeam3 = FeixiaoMarch(
            () -> myRobin(ForTomorrowsJourney::new),
            () -> myGallagher(QuidProQuo::new)
    );

    static final List<AbstractCharacter<?>> baseTeam4 = FeixiaoMarch(
            () -> myRobin(ForTomorrowsJourney::new),
            () -> myBroyna(ButTheBattleIsntOver::new)
    );

    static final List<List<AbstractCharacter<?>>> teams  = new ArrayList<>();
    static final List<List<AbstractCharacter<?>>> teams2  = new ArrayList<>();
    static final List<List<AbstractCharacter<?>>> teams3  = new ArrayList<>();
    static final List<List<AbstractCharacter<?>>> teams4  = new ArrayList<>();


    static {

        repeater((LightConeSupplier robinLightcone) -> {
            repeater((LightConeSupplier feixiaoLightcone) -> {
                repeater((LightConeSupplier marchLightcone) -> {
                    teams.add(
                      of(
                              myFeixiao(feixiaoLightcone),
                              myMarch(marchLightcone),
                              myRobin(robinLightcone),
                              myGallagher()
                      )
                    );
                }, CruisingInTheStellarSea::new, Swordplay::new, OnlySilenceRemains::new);
            }, IVentureForthToHunt::new, CruisingInTheStellarSea::new);
        }, ForTomorrowsJourney::new, PoisedToBloom::new, FlowingNightglow::new);

        repeater((LightConeSupplier robinLightcone) -> {
            repeater((LightConeSupplier feixiaoLightcone) -> {
                repeater((LightConeSupplier marchLightcone) -> {
                    repeater((Boolean b) -> {
                        teams2.add(
                                of(
                                        myFeixiao(feixiaoLightcone, b),
                                        myMarch(marchLightcone),
                                        myRobin(robinLightcone),
                                        myBroyna()
                                )
                        );
                    }, true, false);
                }, CruisingInTheStellarSea::new, Swordplay::new, OnlySilenceRemains::new);
            }, IVentureForthToHunt::new, CruisingInTheStellarSea::new);
        }, ForTomorrowsJourney::new, PoisedToBloom::new, FlowingNightglow::new);

        teams3.add(
                of(
                        myFeixiao(),
                        myMarch(OnlySilenceRemains::new),
                        robinSpd(ForTomorrowsJourney::new),
                        myGallagher()
                )
        );

        teams4.add(
                of(
                        myFeixiao(),
                        myMarch(OnlySilenceRemains::new),
                        robinSpd(ForTomorrowsJourney::new),
                        myBroyna(ButTheBattleIsntOver::new)
                )
        );
    }

    public static void runTests() {
        //runTests(baseTeam, teams);
        //System.out.println("\n\n\n");
        //runTests(baseTeam2, teams2);
        //System.out.println("\n\n\n");
        runTests(baseTeam3, teams3, 150);
        System.out.println("\n\n\n");
        runTests(baseTeam4, teams4, 150);
    }

    private static void runTests(List<AbstractCharacter<?>> baseTeam, List<List<AbstractCharacter<?>>> teams) {
        runTests(baseTeam, teams, 550);
    }

    private static void runTests(List<AbstractCharacter<?>> baseTeam, List<List<AbstractCharacter<?>>> teams, int battleAV) {
        List<BattleResult> results = new ArrayList<>();
        setupBattle(baseTeam, results).Start(battleAV);
        teams.parallelStream()
                .forEach(team -> setupBattle(team, results).Start(battleAV));

        compareBattleResults(results);
    }

    private static IBattle setupBattle(List<AbstractCharacter<?>> team, List<BattleResult> results) {
        String key = team
                .stream()
                .map(c -> c.getClass().getSimpleName() + "(" + c.lightcone.getClass().getSimpleName() + ")")
                .collect(Collectors.joining(" - "));
        PrintStream printStream;
        try {
            printStream = new PrintStream(new FileOutputStream("export_tests/" + key + ".log"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BattleResult result = new BattleResult();
        result.team = team;
        IBattle battle = new Battle(b -> new MetricLogger(b, printStream) {

            @Override
            public void handle(FinalDmgMetrics finalDmgMetrics) {
                super.handle(finalDmgMetrics);
                result.finalDmgMetrics = finalDmgMetrics;
                synchronized (results) {
                    results.add(result);
                }
            }

            @Override
            public void handle(BattleMetrics battleMetrics) {
                super.handle(battleMetrics);
                result.battleMetrics = battleMetrics;
            }

            @Override
            public void handle(PostCombatPlayerMetrics postCombatPlayerMetrics) {
                super.handle(postCombatPlayerMetrics);
                result.postCombatPlayerMetrics = postCombatPlayerMetrics;
            }

            @Override
            public void handle(TotalDamage totalDamage) {
                this.out.println(this.getGson().toJson(totalDamage));
                this.out.println();
            }
        });
        List<AbstractEnemy> enemyTeam = new ArrayList<>();
        enemyTeam.add(new Hoolay());
        //enemyTeam.add(new FireWindImgLightningWeakEnemy(0, 0));
        //enemyTeam.add(new PhysFireWeakEnemy(0, 0));
        battle.setEnemyTeam(enemyTeam);
        battle.setPlayerTeam(team);

        return battle;
    }

    private static void compareBattleResults(List<BattleResult> results) {
        List<String> headers = new ArrayList<>();
        headers.add("Character 1");
        headers.add("Character 2");
        headers.add("Character 3");
        headers.add("Character 4");
        headers.add("DMG");
        headers.add("DPAV");
        headers.add("% base");
        headers.add("% last");
        List<List<String>> columns = new ArrayList<>();

        columns.add(formatResult(results.get(0)));
        columns.add(of("", "", "", "", "", ""));

        float baseFinalDPAV = results.get(0).battleMetrics.finalDPAV;
        float lastFinalDPAV = results.get(0).battleMetrics.finalDPAV;

        results.remove(0);
        results = results
                .stream()
                .sorted((a, b) -> {
                    List<String> ca = a.team
                            .stream()
                            .map(c -> String.format("%s(%s) %s", c.name, c.lightcone.toString(), c.nameSuffix))
                            .collect(Collectors.toList());

                    List<String> cb = b.team
                            .stream()
                            .map(c -> String.format("%s(%s) %s", c.name, c.lightcone.toString(), c.nameSuffix))
                            .collect(Collectors.toList());

                    return new StringListComparator().compare(ca, cb);
                }).collect(Collectors.toList());

        for (BattleResult result : results) {
            List<String> res = formatResult(result, baseFinalDPAV, lastFinalDPAV);
            if (res != null)
                columns.add(res);


            lastFinalDPAV = result.battleMetrics.finalDPAV;
        }

        printTable(System.out, headers, columns);
    }

    private static List<String> formatResult(BattleResult result) {
        return formatResult(result, -1, -1);
    }

    private static List<String> formatResult(BattleResult result,float baseFinalDPAV, float lastFinalDPAV) {
        int totalPlayerDmg = result.finalDmgMetrics.totalPlayerDmg;
        float finalDPAV = result.battleMetrics.finalDPAV;

        List<String> columns = result.team
                .stream()
                .map(c -> String.format("%s(%s) %s", c.name, c.lightcone.toString(), c.nameSuffix))
                .collect(Collectors.toList());

        columns.add("" + totalPlayerDmg);
        columns.add("" + finalDPAV);
        if (baseFinalDPAV != -1) {
            float per = calculatePercentageChange(baseFinalDPAV, finalDPAV);
            if (per == 0) {
                return null;
            }
            float per2 = calculatePercentageChange(lastFinalDPAV, finalDPAV);
            if (per2 == 0) {
                return null;
            }
            columns.add(String.format("%s(%+.2f%%)%s", colour(per), per, RESET));
            columns.add(String.format("%s(%+.2f%%)%s", colour(per2), per2, RESET));
        } else {
            columns.add("");
            columns.add("");
        }

        return columns;
    }

    private static String colour(float per) {
        if (per > 20) {
            return BLUE;
        }
        if (per > 0) {
            return GREEN;
        }
        if (per < -20) {
            return RED;
        }
        if (per < 0) {
            return YELLOW;
        }
        return RESET;
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
        int diff = s.length() - getStrippedLength(s);
        return String.format("%-" + (n+diff) + "s", s);
    }

    @SafeVarargs
    static <T> List<T> of(T... elements) {
        return Arrays.asList(elements);
    }

    @SafeVarargs
    private static <T> void repeater(Consumer<T> c, T ...s) {
        for (T t : s) {
            c.accept(t);
        }
    }

    public static class StringListComparator implements Comparator<List<String>> {

        @Override
        public int compare(List<String> o1, List<String> o2) {
            int maxIndex = Math.min(o1.size(), o2.size());
            for (int i = 0; i < maxIndex; i++) {
                int res = o1.get(i).compareTo(o2.get(i));
                if (res != 0) {
                    return res;
                }
            }
            return Integer.compare(o1.size(), o2.size());
        }
    }

}
