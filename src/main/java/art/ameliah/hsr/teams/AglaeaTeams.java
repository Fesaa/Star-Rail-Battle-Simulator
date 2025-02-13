package art.ameliah.hsr.teams;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.harmony.sunday.Sunday;
import art.ameliah.hsr.characters.remembrance.aglaea.Aglaea;
import art.ameliah.hsr.lightcones.harmony.AGroundedAscent;
import art.ameliah.hsr.lightcones.harmony.ButTheBattleIsntOver;
import art.ameliah.hsr.lightcones.remembrance.TimeWovenIntoGold;
import art.ameliah.hsr.relics.RelicStats;
import art.ameliah.hsr.relics.Stats;
import art.ameliah.hsr.relics.ornament.SpringhtlyVonwacq;
import art.ameliah.hsr.relics.ornament.TheWondrousBananAmusementPark;
import art.ameliah.hsr.relics.relics.HeroOfTriumphantSong;
import art.ameliah.hsr.relics.relics.SacerdosRelivedOrdeal;

import java.util.ArrayList;

public class AglaeaTeams {
    public static AbstractCharacter<?> get2xSpeedAglaea() {
        AbstractCharacter<?> character = new Aglaea();
        character.EquipLightcone(new TimeWovenIntoGold(character));
        character.EquipRelicSet(new HeroOfTriumphantSong(character));
        character.EquipRelicSet(new TheWondrousBananAmusementPark(character));
        RelicStats relicStats = new RelicStats();
        relicStats.addMainStat(Stats.CRIT_RATE).addMainStat(Stats.SPEED).
                addMainStat(Stats.LIGHTNING_DAMAGE).addMainStat(Stats.ERR);
        relicStats.addSubStat(Stats.CRIT_RATE, 11).addSubStat(Stats.CRIT_DAMAGE, 4).addSubStat(Stats.SPEED, 9);
        relicStats.equipTo(character);
        return character;
    }

    public static AbstractCharacter<?> get134SpeedSunday() {
        AbstractCharacter<?> character = new Sunday();
        character.EquipLightcone(new AGroundedAscent(character));
        character.EquipRelicSet(new SacerdosRelivedOrdeal(character));
        character.EquipRelicSet(new SpringhtlyVonwacq(character));
        RelicStats relicStats = new RelicStats();
        relicStats.addMainStat(Stats.CRIT_DAMAGE).addMainStat(Stats.SPEED).
                addMainStat(Stats.HP_PER).addMainStat(Stats.ERR);
        relicStats.addSubStat(Stats.EFFECT_RES, 7).addSubStat(Stats.CRIT_DAMAGE, 10).addSubStat(Stats.SPEED, 3);
        relicStats.equipTo(character);
        return character;
    }

    public static AbstractCharacter<?> getPrebuiltRobin() {
        return PlayerTeam.getPrebuiltRobin();
    }

    public static AbstractCharacter<?> getPrebuiltHuohuo() {
        return PlayerTeam.getPrebuiltHuohuo();
    }

    public static class DoubleSpeedAglaeaTeam extends PlayerTeam {
        @Override
        public ArrayList<AbstractCharacter<?>> getTeam() {
            ArrayList<AbstractCharacter<?>> playerTeam = new ArrayList<>();
            playerTeam.add(get2xSpeedAglaea());
            playerTeam.add(get134SpeedSunday());
            playerTeam.add(getPrebuiltRobin());
            playerTeam.add(getPrebuiltHuohuo());
            return playerTeam;
        }
    }
}
