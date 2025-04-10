package art.ameliah.hsr.teams;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.abundance.gallagher.Gallagher;
import art.ameliah.hsr.characters.abundance.huohuo.Huohuo;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysSkillGoal;
import art.ameliah.hsr.characters.harmony.ruanmei.RuanMei;
import art.ameliah.hsr.characters.harmony.sunday.Sunday;
import art.ameliah.hsr.characters.remembrance.castorice.Castorice;
import art.ameliah.hsr.characters.remembrance.trailblazer.Trailblazer;
import art.ameliah.hsr.lightcones.abundance.HeyOverHere;
import art.ameliah.hsr.lightcones.abundance.QuidProQuo;
import art.ameliah.hsr.lightcones.harmony.MemoriesOfThePast;
import art.ameliah.hsr.lightcones.harmony.PastAndFuture;
import art.ameliah.hsr.lightcones.remembrance.MakeFarewellsMoreBeautiful;
import art.ameliah.hsr.lightcones.remembrance.VictoryInABlink;
import art.ameliah.hsr.relics.RelicStats;
import art.ameliah.hsr.relics.Stats;
import art.ameliah.hsr.relics.ornament.BoneCollectionsSereneDemesne;
import art.ameliah.hsr.relics.ornament.BrokenKeel;
import art.ameliah.hsr.relics.ornament.ForgeOfTheKalpagniLatern;
import art.ameliah.hsr.relics.ornament.SpringhtlyVonwacq;
import art.ameliah.hsr.relics.relics.GiantTreeOfRaptBrooding;
import art.ameliah.hsr.relics.relics.HeroOfTriumphantSong;
import art.ameliah.hsr.relics.relics.MessengerTraversingHackerspace;
import art.ameliah.hsr.relics.relics.PasserbyOfWanderingCloud;
import art.ameliah.hsr.relics.relics.PoetOfMourningCollapse;
import art.ameliah.hsr.relics.relics.SacerdosRelivedOrdeal;
import art.ameliah.hsr.relics.relics.ThiefOfShootingMeteor;
import art.ameliah.hsr.relics.relics.WatchMakerMasterOfDreamMachinations;

import java.util.ArrayList;

public class CastoriceTeams {

    private static AbstractCharacter<?> myCastorice() {
        Castorice castorice = new Castorice();
        castorice.EquipLightcone(new MakeFarewellsMoreBeautiful(castorice));
        castorice.EquipRelicSet(new PoetOfMourningCollapse(castorice, true));
        castorice.EquipRelicSet(new BoneCollectionsSereneDemesne(castorice));
        new RelicStats()
                .addMainStat(Stats.CRIT_DAMAGE)
                .addMainStat(Stats.HP_PER)
                .addMainStat(Stats.HP_PER)
                .addMainStat(Stats.HP_PER)
                .addSubStat(Stats.CRIT_RATE, 14)
                .addSubStat(Stats.CRIT_DAMAGE, 18)
                .addSubStat(Stats.HP_PER, 3)
                .addSubStat(Stats.HP_FLAT, 3)
                .equipTo(castorice);

        return castorice;
    }

    private static AbstractCharacter<?> mySunday() {
        Sunday sunday = new Sunday();
        sunday.EquipLightcone(new PastAndFuture(sunday));
        sunday.EquipRelicSet(new SacerdosRelivedOrdeal(sunday, true));
        sunday.EquipRelicSet(new BrokenKeel(sunday));
        new RelicStats()
                .addMainStat(Stats.CRIT_DAMAGE)
                .addMainStat(Stats.SPEED)
                .addMainStat(Stats.HP_PER)
                .addMainStat(Stats.ERR)
                .addSubStat(Stats.CRIT_DAMAGE, 18)
                .addSubStat(Stats.SPEED, 3)
                .equipTo(sunday);

        return sunday;
    }

    private static AbstractCharacter<?> myRuanMei() {
        RuanMei ruanMei = new RuanMei();
        ruanMei.EquipLightcone(new MemoriesOfThePast(ruanMei));
        ruanMei.EquipRelicSet(new ThiefOfShootingMeteor(ruanMei, false));
        ruanMei.EquipRelicSet(new WatchMakerMasterOfDreamMachinations(ruanMei, false));
        ruanMei.EquipRelicSet(new SpringhtlyVonwacq(ruanMei));
        new RelicStats()
                .addMainStat(Stats.HP_PER)
                .addMainStat(Stats.HP_PER)
                .addMainStat(Stats.HP_PER)
                .addMainStat(Stats.ERR)
                .addSubStat(Stats.BREAK_EFFECT, 12)
                .addSubStat(Stats.SPEED, 5)
                .addSubStat(Stats.HP_PER, 3)
                .addSubStat(Stats.HP_FLAT, 10)
                .equipTo(ruanMei);

        return ruanMei;
    }

    private static AbstractCharacter<?> myRMC() {
        Trailblazer rmc = new Trailblazer();
        rmc.EquipLightcone(new VictoryInABlink(rmc));
        rmc.EquipRelicSet(new HeroOfTriumphantSong(rmc, true));
        rmc.EquipRelicSet(new SpringhtlyVonwacq(rmc));
        new RelicStats()
                .addMainStat(Stats.CRIT_DAMAGE)
                .addMainStat(Stats.SPEED)
                .addMainStat(Stats.HP_PER)
                .addMainStat(Stats.ERR)
                .addSubStat(Stats.CRIT_DAMAGE, 5)
                .addSubStat(Stats.SPEED, 11)
                .addSubStat(Stats.HP_PER, 2)
                .addSubStat(Stats.HP_FLAT, 1)
                .equipTo(rmc);

        rmc.clearTurnGoals();
        rmc.registerGoal(0, new AlwaysSkillGoal<>(rmc, 1));

        return rmc;
    }

    private static AbstractCharacter<?> myGallagher() {
        Gallagher gallagher = new Gallagher();
        gallagher.EquipLightcone(new QuidProQuo(gallagher));
        gallagher.EquipRelicSet(new SacerdosRelivedOrdeal(gallagher));
        gallagher.EquipRelicSet(new MessengerTraversingHackerspace(gallagher));
        gallagher.EquipRelicSet(new ForgeOfTheKalpagniLatern(gallagher));
        new RelicStats()
                .addMainStat(Stats.HEALING)
                .addMainStat(Stats.SPEED)
                .addMainStat(Stats.DEF_PER)
                .addMainStat(Stats.BREAK_EFFECT)
                .addSubStat(Stats.SPEED, 6)
                .addSubStat(Stats.HP_PER, 11)
                .addSubStat(Stats.HP_FLAT, 1)
                .addSubStat(Stats.BREAK_EFFECT, 4)
                .equipTo(gallagher);

        return gallagher;
    }

    private static AbstractCharacter<?> myHuoHuo() {
        Huohuo huohuo = new Huohuo();
        huohuo.EquipLightcone(new HeyOverHere(huohuo));
        huohuo.EquipRelicSet(new PasserbyOfWanderingCloud(huohuo, true));
        huohuo.EquipRelicSet(new GiantTreeOfRaptBrooding(huohuo));
        new RelicStats()
                .addMainStat(Stats.HEALING)
                .addMainStat(Stats.SPEED)
                .addMainStat(Stats.HP_PER)
                .addMainStat(Stats.ERR)
                .addSubStat(Stats.HP_PER, 17)
                .addSubStat(Stats.HP_FLAT, 8)
                .addSubStat(Stats.SPEED, 1)
                .equipTo(huohuo);

        huohuo.clearTurnGoals();
        huohuo.registerGoal(0, new AlwaysSkillGoal<>(huohuo));
        return huohuo;
    }

    public static class CastoriceTestTeam extends PlayerTeam {
        @Override
        public ArrayList<AbstractCharacter<?>> getTeam() {
            ArrayList<AbstractCharacter<?>> team = new ArrayList<>();
            team.add(myCastorice());
            team.add(myRuanMei());
            //team.add(mySunday());
            team.add(myRMC());
            team.add(myHuoHuo());
            //team.add(myGallagher());

            return team;
        }
    }

}
