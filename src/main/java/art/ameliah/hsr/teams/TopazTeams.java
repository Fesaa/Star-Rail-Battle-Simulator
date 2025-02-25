package art.ameliah.hsr.teams;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.destruction.yunli.Yunli;
import art.ameliah.hsr.characters.hunt.drRatio.DrRatio;
import art.ameliah.hsr.characters.hunt.march.SwordMarch;
import art.ameliah.hsr.characters.hunt.moze.Moze;
import art.ameliah.hsr.characters.hunt.topaz.Topaz;
import art.ameliah.hsr.characters.preservation.aventurine.Aventurine;
import art.ameliah.hsr.lightcones.destruction.DanceAtSunset;
import art.ameliah.hsr.lightcones.hunt.BaptismOfPureThought;
import art.ameliah.hsr.lightcones.hunt.WorrisomeBlissful;
import art.ameliah.hsr.lightcones.preservation.ConcertForTwo;
import art.ameliah.hsr.relics.RelicStats;
import art.ameliah.hsr.relics.Stats;
import art.ameliah.hsr.relics.ornament.BrokenKeel;
import art.ameliah.hsr.relics.ornament.DuranDynastyOfRunningWolves;
import art.ameliah.hsr.relics.ornament.RutilentArena;
import art.ameliah.hsr.relics.relics.KnightOfPurityPalace;
import art.ameliah.hsr.relics.relics.MusketeerOfWildWheat;
import art.ameliah.hsr.relics.relics.PioneerDiverOfDeadWaters;
import art.ameliah.hsr.relics.relics.TheAshblazingGrandDuke;
import art.ameliah.hsr.relics.relics.TheWindSoaringValorous;

import java.util.ArrayList;

public class TopazTeams {
    public static AbstractCharacter<?> getPrebuiltRatioTT() {
        AbstractCharacter<?> character = new DrRatio();
        character.EquipLightcone(new BaptismOfPureThought(character));
        character.EquipRelicSet(new PioneerDiverOfDeadWaters(character));
        character.EquipRelicSet(new DuranDynastyOfRunningWolves(character));
        RelicStats relicStats = new RelicStats();
        relicStats.addMainStat(Stats.CRIT_RATE).addMainStat(Stats.SPEED).
                addMainStat(Stats.IMAGINARY_DAMAGE).addMainStat(Stats.ATK_PER);
        relicStats.addSubStat(Stats.CRIT_RATE, 9).addSubStat(Stats.CRIT_DAMAGE, 12).addSubStat(Stats.SPEED, 3);
        relicStats.equipTo(character);
        return character;
    }

    public static AbstractCharacter<?> getPrebuiltRobinTT() {
        return PlayerTeam.getPrebuiltRobin();
    }

    public static AbstractCharacter<?> getPrebuiltTopazTT() {
        AbstractCharacter<?> character = new Topaz();
        character.EquipLightcone(new WorrisomeBlissful(character));
        character.EquipRelicSet(new TheAshblazingGrandDuke(character));
        character.EquipRelicSet(new DuranDynastyOfRunningWolves(character));
        RelicStats relicStats = new RelicStats();
        relicStats.addMainStat(Stats.CRIT_RATE).addMainStat(Stats.SPEED).
                addMainStat(Stats.FIRE_DAMAGE).addMainStat(Stats.ATK_PER);
        relicStats.addSubStat(Stats.CRIT_RATE, 7).addSubStat(Stats.CRIT_DAMAGE, 16).addSubStat(Stats.SPEED, 1);
        relicStats.equipTo(character);
        return character;
    }

    public static AbstractCharacter<?> getPrebuiltAventurineTT() {
        AbstractCharacter<?> character = new Aventurine(false);
        character.EquipLightcone(new ConcertForTwo(character));
        character.EquipRelicSet(new KnightOfPurityPalace(character, false));
        character.EquipRelicSet(new TheAshblazingGrandDuke(character, false));
        character.EquipRelicSet(new BrokenKeel(character));
        RelicStats relicStats = new RelicStats();
        relicStats.addMainStat(Stats.DEF_PER).addMainStat(Stats.SPEED).
                addMainStat(Stats.DEF_PER).addMainStat(Stats.DEF_PER);
        relicStats.addSubStat(Stats.CRIT_RATE, 10).addSubStat(Stats.DEF_PER, 3).
                addSubStat(Stats.CRIT_DAMAGE, 7).addSubStat(Stats.SPEED, 8);
        relicStats.equipTo(character);
        return character;
    }

    public static AbstractCharacter<?> getPrebuiltFeixiaoTT() {
        return PlayerTeam.getPrebuiltFeixiao();
    }

    public static AbstractCharacter<?> getPrebuiltSwordMarchTT() {
        AbstractCharacter<?> character = new SwordMarch();
        character.EquipLightcone(new WorrisomeBlissful(character));
        character.EquipRelicSet(new MusketeerOfWildWheat(character));
        character.EquipRelicSet(new RutilentArena(character));
        RelicStats relicStats = new RelicStats();
        relicStats.addMainStat(Stats.CRIT_RATE).addMainStat(Stats.SPEED).
                addMainStat(Stats.IMAGINARY_DAMAGE).addMainStat(Stats.ATK_PER);
        relicStats.addSubStat(Stats.CRIT_RATE, 10).addSubStat(Stats.CRIT_DAMAGE, 11).addSubStat(Stats.SPEED, 3);
        relicStats.equipTo(character);
        return character;
    }

    public static AbstractCharacter<?> getPrebuiltMozeTT() {
        AbstractCharacter<?> character = new Moze();
        character.EquipLightcone(new WorrisomeBlissful(character));
        character.EquipRelicSet(new PioneerDiverOfDeadWaters(character));
        character.EquipRelicSet(new DuranDynastyOfRunningWolves(character));
        RelicStats relicStats = new RelicStats();
        relicStats.addMainStat(Stats.CRIT_RATE).addMainStat(Stats.ATK_PER).
                addMainStat(Stats.LIGHTNING_DAMAGE).addMainStat(Stats.ATK_PER);
        relicStats.addSubStat(Stats.CRIT_RATE, 10).addSubStat(Stats.CRIT_DAMAGE, 14);
        relicStats.equipTo(character);
        return character;
    }

    public static AbstractCharacter<?> getPrebuiltYunliTT() {
        AbstractCharacter<?> character = new Yunli();
        character.isDPS = false;
        character.EquipLightcone(new DanceAtSunset(character));
        character.EquipRelicSet(new TheWindSoaringValorous(character));
        character.EquipRelicSet(new DuranDynastyOfRunningWolves(character));
        RelicStats relicStats = new RelicStats();
        relicStats.addMainStat(Stats.CRIT_RATE).addMainStat(Stats.ATK_PER).
                addMainStat(Stats.PHYSICAL_DAMAGE).addMainStat(Stats.ATK_PER);
        relicStats.addSubStat(Stats.CRIT_RATE, 13).addSubStat(Stats.CRIT_DAMAGE, 11);
        relicStats.equipTo(character);
        return character;
    }

    public static AbstractCharacter<?> getPrebuiltHuohuoTT() {
        return PlayerTeam.getPrebuiltHuohuo();
    }

    public static class RatioRobinAventurineTopaz extends PlayerTeam {
        @Override
        public ArrayList<AbstractCharacter<?>> getTeam() {
            ArrayList<AbstractCharacter<?>> playerTeam = new ArrayList<>();
            playerTeam.add(getPrebuiltRatioTT());
            playerTeam.add(getPrebuiltRobinTT());
            playerTeam.add(getPrebuiltAventurineTT());
            playerTeam.add(getPrebuiltTopazTT());
            return playerTeam;
        }
    }

    public static class FeixiaoRobinAventurineTopaz extends PlayerTeam {
        @Override
        public ArrayList<AbstractCharacter<?>> getTeam() {
            ArrayList<AbstractCharacter<?>> playerTeam = new ArrayList<>();
            playerTeam.add(getPrebuiltFeixiaoTT());
            playerTeam.add(getPrebuiltRobinTT());
            playerTeam.add(getPrebuiltAventurineTT());
            playerTeam.add(getPrebuiltTopazTT());
            return playerTeam;
        }
    }

    public static class MarchRobinAventurineTopaz extends PlayerTeam {
        @Override
        public ArrayList<AbstractCharacter<?>> getTeam() {
            ArrayList<AbstractCharacter<?>> playerTeam = new ArrayList<>();
            playerTeam.add(getPrebuiltSwordMarchTT());
            playerTeam.add(getPrebuiltRobinTT());
            playerTeam.add(getPrebuiltAventurineTT());
            AbstractCharacter<?> topaz = getPrebuiltTopazTT();
            topaz.isDPS = true;
            playerTeam.add(topaz);
            return playerTeam;
        }
    }

    public static class MozeRobinAventurineTopaz extends PlayerTeam {
        @Override
        public ArrayList<AbstractCharacter<?>> getTeam() {
            ArrayList<AbstractCharacter<?>> playerTeam = new ArrayList<>();
            playerTeam.add(getPrebuiltMozeTT());
            playerTeam.add(getPrebuiltRobinTT());
            playerTeam.add(getPrebuiltAventurineTT());
            AbstractCharacter<?> topaz = getPrebuiltTopazTT();
            topaz.isDPS = true;
            playerTeam.add(topaz);
            return playerTeam;
        }
    }

    public static class HuohuoYunliRobinTopaz extends PlayerTeam {
        @Override
        public ArrayList<AbstractCharacter<?>> getTeam() {
            ArrayList<AbstractCharacter<?>> playerTeam = new ArrayList<>();
            playerTeam.add(getPrebuiltHuohuoTT());
            playerTeam.add(getPrebuiltYunliTT());
            playerTeam.add(getPrebuiltRobinTT());
            AbstractCharacter<?> topaz = getPrebuiltTopazTT();
            topaz.isDPS = true;
            playerTeam.add(topaz);
            return playerTeam;
        }
    }
}
