package art.ameliah.hsr;

import art.ameliah.hsr.battleLogic.Battle;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.log.DefaultLogger;
import art.ameliah.hsr.battleLogic.log.lines.battle.TurnEnd;
import art.ameliah.hsr.battleLogic.log.lines.battle.TurnStart;
import art.ameliah.hsr.battleLogic.log.lines.metrics.FinalDmgMetrics;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.AllWeakEnemy;
import art.ameliah.hsr.enemies.FireWindImgLightningWeakEnemy;
import art.ameliah.hsr.teams.PlayerTeam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import static art.ameliah.hsr.teams.PlayerTeam.FeixiaoBronyaAventurineMoze;

public class Main {

    public static String commitHash = TestHelper.commitHash();

    public static void main(String[] args) {
        Locale.setDefault(Locale.UK);

        //debugTeam();
        ameliasSanityCheck();
        //WaveTester.MocTest();
        //WaveTester.MocDivTest();
        //WaveTester.PfTest();
    }

    public static void debugTeam() {
        Battle battle = new Battle();
        battle.lessMetrics = true;

        //     battle.setPlayerTeam(new TingyunYunliRobinHuohuoTeam().getTeam());
//        //battle.setPlayerTeam(new TopazYunliRobinHuohuoTeam().getTeam());
//        //battle.setPlayerTeam(new MarchYunliRobinHuohuoTeam().getTeam());
//       battle.setPlayerTeam(new SparkleYunliRobinHuohuoTeam().getTeam());
////        battle.setPlayerTeam(new SparkleYunliTingyunHuohuoTeam().getTeam());
////        battle.setPlayerTeam(new PelaYunliTingyunHuohuoTeam().getTeam());
////        battle.setPlayerTeam(new TopazYunliRobinAventurineTeam().getTeam());
//
//        ArrayList<AbstractEnemy> enemyTeam = new ArrayList<>();
//        enemyTeam.add(new PhysWeakEnemy(0, 2));
//        enemyTeam.add(new PhysWeakEnemy(1, 2));
//        enemyTeam.add(new PhysWeakEnemy(2, 2));
//        battle.setEnemyTeam(enemyTeam);

        //battle.setPlayerTeam(new FeixiaoRobinAventurineTopaz().getTeam());
        //battle.setPlayerTeam(new FeixiaoRobinAventurineMarch().getTeam());
        //battle.setPlayerTeam(new FeixiaoRobinAventurineMoze().getTeam());
        //battle.setPlayerTeam(new FeixiaoSparkleAventurineTopaz().getTeam());
        //battle.setPlayerTeam(new FeixiaoSparkleAventurineMarch().getTeam());
        //battle.setPlayerTeam(new FeixiaoRuanMeiAventurineTopaz().getTeam());
        //battle.setPlayerTeam(new FeixiaoRuanMeiAventurineMarch().getTeam());
        //battle.setPlayerTeam(new FeixiaoPelaAventurineTopaz().getTeam());
        //battle.setPlayerTeam(new FeixiaoPelaAventurineMarch().getTeam());
        //battle.setPlayerTeam(new FeixiaoPelaGallagherMarch().getTeam());
        //battle.setPlayerTeam(new FeixiaoRobinAventurinePela().getTeam());
        //battle.setPlayerTeam(new FeixiaoTopazAventurineMarch().getTeam());
        //battle.setPlayerTeam(new FeixiaoHanyaAventurineTopaz().getTeam());
        //battle.setPlayerTeam(new FeixiaoHanyaAventurineMarch().getTeam());
        //battle.setPlayerTeam(new FeixiaoRuanMeiGallagherTopaz().getTeam());
        //battle.setPlayerTeam(new FeixiaoRuanMeiGallagherMarch().getTeam());
        //battle.setPlayerTeam(new FeixiaoRobinGallagherTopaz().getTeam());
        //battle.setPlayerTeam(new FeixiaoRobinGallagherMarch().getTeam());
        //battle.setPlayerTeam(new FeixiaoRobinTopazFuXuan().getTeam());
        //battle.setPlayerTeam(new FeixiaoRobinMarchFuXuan().getTeam());
        //battle.setPlayerTeam(new FeixiaoHanyaGallagherMarch().getTeam());
        //battle.setPlayerTeam(new AstaFeixiaoGallagherMarch().getTeam());
        //battle.setPlayerTeam(new FeixiaoBronyaAventurineTopaz().getTeam());
        //battle.setPlayerTeam(new FeixiaoBronyaAventurineMarch().getTeam());
        //battle.setPlayerTeam(new FeixiaoHanyaGallagherMarch().getTeam());
        //battle.setPlayerTeam(new FeixiaoRobinGallagherBronya().getTeam());
        //battle.setPlayerTeam(new FeixiaoRobinLingshaTopaz().getTeam());
        //battle.setPlayerTeam(new FeixiaoRuanMeiLingshaTopaz().getTeam());
        //battle.setPlayerTeam(new FeixiaoRobinLingshaMarch().getTeam());
        //battle.setPlayerTeam(new PelaFeixiaoGallagherMarch().getTeam());
        //battle.setPlayerTeam(new FeixiaoMozeGallagherMarch().getTeam());
        //battle.setPlayerTeam(new FeixiaoRuanMeiAventurineMarch().getTeam());
        //battle.setPlayerTeam(new TopazTeams.RatioRobinAventurineTopaz().getTeam());
        //battle.setPlayerTeam(new TopazTeams.FeixiaoRobinAventurineTopaz().getTeam());
        //battle.setPlayerTeam(new TopazTeams.MarchRobinAventurineTopaz().getTeam());
        //battle.setPlayerTeam(new TopazTeams.MozeRobinAventurineTopaz().getTeam());
        //battle.setPlayerTeam(new TopazTeams.HuohuoYunliRobinTopaz().getTeam());
        //battle.setPlayerTeam(new PelaYunliRobinHuohuoTeam().getTeam());
        battle.setPlayerTeam(new FeixiaoBronyaAventurineMoze().getTeam());

        ArrayList<AbstractEnemy> enemyTeam = new ArrayList<>();
        //enemyTeam.add(new WindWeakEnemy(0, 0));
        //enemyTeam.add(new FireWindImgLightningWeakEnemy(0, 0));
        enemyTeam.add(new AllWeakEnemy(0, 2));
        enemyTeam.add(new AllWeakEnemy(1, 2));
        battle.setEnemyTeam(enemyTeam);

        battle.Start(550);
    }

    @SuppressWarnings("unchecked")
    public static void ameliasSanityCheck() {
        AtomicInteger length = new AtomicInteger();

        List<Pair<String, ArrayList<AbstractCharacter<?>>>> teams = TestHelper.getStaticClassesExtendingA(PlayerTeam.class, PlayerTeam.class)
                .stream()
                .map(c -> new Pair<>(c.getSimpleName(), (ArrayList<AbstractCharacter<?>>) TestHelper.callMethodOnClasses(c, "getTeam")))
                .sorted(Comparator.comparing(Pair::key))
                .peek(p -> length.set(Math.max(length.get(), p.key().length() + 3)))
                .toList();

        for (Pair<String, ArrayList<AbstractCharacter<?>>> p : teams) {
            IBattle battle = constructBattle(p);
            String Prefix = p.key() + ":" + " ".repeat(Math.max(0, length.get() - (p.key().length() + 1)));

            battle.setPlayerTeam(p.value());
            ArrayList<AbstractEnemy> enemyTeam = new ArrayList<>();
            enemyTeam.add(new FireWindImgLightningWeakEnemy(0, 0));
            battle.setEnemyTeam(enemyTeam);

            System.out.print(Prefix);
            battle.Start(550);
        }
    }

    private static IBattle constructBattle(Pair<String, ArrayList<AbstractCharacter<?>>> p) {
        String path = "export/" + commitHash;
        File dir = new File(path);
        if (!dir.exists() && !dir.mkdir()) {
            throw new RuntimeException("Couldn't create directory " + path);
        }

        PrintStream printStream;
        try {
            printStream = new PrintStream(new FileOutputStream(path + "/" + p.key() + ".log"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return new Battle((b) -> new DefaultLogger(b, printStream) {
            @Override
            public void handle(FinalDmgMetrics finalDmgMetrics) {
                System.out.println(finalDmgMetrics.asString());
                this.log(finalDmgMetrics);
            }

            @Override
            public void handle(TurnStart turnStart) {
                this.out.println();
                this.log(turnStart);
            }

            @Override
            public void handle(TurnEnd turnEnd) {
                this.log(turnEnd);
                this.out.println();
            }
        });
    }

    private record Pair<K, V>(K key, V value) {
    }


}