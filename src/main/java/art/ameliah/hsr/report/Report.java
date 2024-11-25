package art.ameliah.hsr.report;

import art.ameliah.hsr.battleLogic.Battle;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.teams.EnemyTeam;
import art.ameliah.hsr.teams.PlayerTeam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Report {
    private final PlayerTeam baselineTeam;
    private final ArrayList<PlayerTeam> otherTeams;
    private final ArrayList<EnemyTeam> enemyTeams;
    private final int AVLength;
    private final String notes;

    public Report(PlayerTeam baselineTeam, ArrayList<PlayerTeam> otherTeams, ArrayList<EnemyTeam> enemyTeams, int AVLength, String notes) {
        this.baselineTeam = baselineTeam;
        this.otherTeams = otherTeams;
        this.enemyTeams = enemyTeams;
        this.AVLength = AVLength;
        this.notes = notes;
    }

    public void generateCSV() {
        StringBuilder CSV = new StringBuilder();
        HashMap<String, String> characterCSVs = new HashMap<>();
        HashMap<String, String> characterStatsMap = new HashMap<>();
        CSV.append(notes).append("\nYou can check the builds and metrics of specific characters in subsequent sheets\n\n\n");
        for (EnemyTeam enemyTeam : enemyTeams) {
            HashMap<String, HashMap<String, ArrayList<String>>> characterMetricsMap = new HashMap<>();
            HashMap<String, ArrayList<String>> characterTeamList = new HashMap<>();
            HashMap<String, ArrayList<String>> damageContributionMap = new HashMap<>();
            HashMap<String, ArrayList<String>> characterMetricOrderList = new HashMap<>();

            ArrayList<AbstractCharacter<?>> baseline = baselineTeam.getTeam();
            float baselineDPAV;
            HashMap<Float, PlayerTeam> DPAVtracker = new HashMap<>();
            HashMap<PlayerTeam, Float> diffTracker = new HashMap<>();
            Battle battle = new Battle();
            battle.setPlayerTeam(baseline);
            battle.setEnemyTeam(enemyTeam.getTeam());
            battle.Start(AVLength);

            baselineDPAV = battle.finalDPAV;
            DPAVtracker.put(baselineDPAV, baselineTeam);
            diffTracker.put(baselineTeam, 100.0f);
            updateCharacterCSVs(battle, characterMetricsMap, characterTeamList, characterMetricOrderList, damageContributionMap, characterStatsMap, baseline, baselineTeam);

            for (PlayerTeam playerTeam : otherTeams) {
                ArrayList<AbstractCharacter<?>> otherTeam = playerTeam.getTeam();
                battle = new Battle();
                battle.setPlayerTeam(otherTeam);
                battle.setEnemyTeam(enemyTeam.getTeam());
                battle.Start(AVLength);

                float otherTeamDPAV = battle.finalDPAV;
                DPAVtracker.put(otherTeamDPAV, playerTeam);
                float diff = otherTeamDPAV / baselineDPAV * 100;
                diffTracker.put(playerTeam, diff);
                updateCharacterCSVs(battle, characterMetricsMap, characterTeamList, characterMetricOrderList, damageContributionMap, characterStatsMap, otherTeam, playerTeam);
            }
            CSV.append(enemyTeam).append("\n\n");
            CSV.append("Team,DPAV,%,dIFF\n");
            ArrayList<Float> DPAVList = new ArrayList<>();
            for (Map.Entry<Float, PlayerTeam> entry : DPAVtracker.entrySet()) {
                DPAVList.add(entry.getKey());
            }
            Collections.sort(DPAVList);
            for (int i = DPAVList.size() - 1; i >= 0; i--) {
                float DPAV = DPAVList.get(i);
                PlayerTeam t = DPAVtracker.get(DPAV);
                CSV.append(String.format("%s,%.3f,%.2f%%\n", t.toString(), DPAV, diffTracker.get(t)));
            }
            CSV.append("\n").append("\n").append("\n");

            for (Map.Entry<String, HashMap<String, ArrayList<String>>> entry : characterMetricsMap.entrySet()) {
                StringBuilder characterCSV = new StringBuilder();
                if (characterCSVs.containsKey(entry.getKey())) {
                    characterCSV = new StringBuilder(characterCSVs.get(entry.getKey()));
                }
                characterCSV.append(enemyTeam).append("\n").append("\n").append(entry.getKey()).append(" Metrics,");
                ArrayList<String> teamRow = characterTeamList.get(entry.getKey());
                for (String team : teamRow) {
                    characterCSV.append(team).append(",");
                }
                characterCSV.append("\n");
                HashMap<String, ArrayList<String>> metricRows = entry.getValue();
                ArrayList<String> metricOrder = characterMetricOrderList.get(entry.getKey());
                for (String metric : metricOrder) {
                    ArrayList<String> metricRow = metricRows.get(metric);
                    characterCSV.append(metric).append(",");
                    for (String metricValue : metricRow) {
                        characterCSV.append(metricValue).append(",");
                    }
                    characterCSV.append("\n");
                }
                ArrayList<String> damageContribution = damageContributionMap.get(entry.getKey());
                characterCSV.append("Damage Contribution").append(",");
                for (String damage : damageContribution) {
                    characterCSV.append(damage).append(",");
                }
                characterCSV.append("\n").append("\n");
                characterCSVs.put(entry.getKey(), characterCSV.toString());
            }
        }
        for (Map.Entry<String, String> entry : characterStatsMap.entrySet()) {
            String characterCSV = characterCSVs.get(entry.getKey());
            String builder = characterCSV + "\n\n\n" + entry.getValue();
            characterCSVs.put(entry.getKey(), builder);
        }
        File csvOutputFile = new File("csv/summary.csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.println(CSV);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, String> entry : characterCSVs.entrySet()) {
            File characterCSV = new File("csv/" + entry.getKey() + "Metrics.csv");
            try (PrintWriter pw = new PrintWriter(characterCSV)) {
                pw.println(entry.getValue());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateCharacterCSVs(IBattle battle, HashMap<String, HashMap<String, ArrayList<String>>> characterMetricsMap, HashMap<String, ArrayList<String>> characterTeamList, HashMap<String, ArrayList<String>> characterMetricOrderList, HashMap<String, ArrayList<String>> damageContributionMap, HashMap<String, String> characterStatsMap, ArrayList<AbstractCharacter<?>> team, PlayerTeam playerTeam) {
        for (AbstractCharacter<?> character : team) {
            if (!characterMetricsMap.containsKey(character.name)) {
                HashMap<String, ArrayList<String>> map = new HashMap<>();
                characterMetricsMap.put(character.name, map);
            }
            HashMap<String, ArrayList<String>> characterMetricMap = characterMetricsMap.get(character.name);
            HashMap<String, String> characterMetrics = character.getCharacterSpecificMetricMap();
            for (Map.Entry<String, String> entry : characterMetrics.entrySet()) {
                if (!characterMetricMap.containsKey(entry.getKey())) {
                    ArrayList<String> list = new ArrayList<>();
                    characterMetricMap.put(entry.getKey(), list);
                }
                ArrayList<String> metricList = characterMetricMap.get(entry.getKey());
                metricList.add(entry.getValue());
            }

            if (!characterTeamList.containsKey(character.name)) {
                ArrayList<String> teamList = new ArrayList<>();
                characterTeamList.put(character.name, teamList);
            }
            ArrayList<String> list = characterTeamList.get(character.name);
            list.add(playerTeam.toString());

            if (!damageContributionMap.containsKey(character.name)) {
                ArrayList<String> damageContributionList = new ArrayList<>();
                damageContributionMap.put(character.name, damageContributionList);
            }
            ArrayList<String> damageList = damageContributionMap.get(character.name);
            float damagePercent = battle.getDamageContributionMapPercent().get(character);
            float rawDamage = battle.getDamageContributionMap().get(character);
            float DPAV = rawDamage / battle.initialLength();
            damageList.add(String.format("%.2f DPAV (%.1f%% of total)", DPAV, damagePercent));

            characterMetricOrderList.put(character.name, character.getOrderedCharacterSpecificMetricsKeys());

            StringBuilder statsCSV = new StringBuilder("Out of combat stats\n");
            for (String stat : character.statsOrder) {
                String statValue = character.statsMap.get(stat);
                statsCSV.append(stat).append(",").append(statValue).append("\n");
            }
            characterStatsMap.put(character.name, statsCSV.toString());
        }
    }
}
