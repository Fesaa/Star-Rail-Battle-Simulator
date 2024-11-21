package art.ameliah.hsr.teams;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.feixiao.Feixiao;
import art.ameliah.hsr.characters.huohuo.Huohuo;
import art.ameliah.hsr.characters.robin.Robin;
import art.ameliah.hsr.characters.topaz.Topaz;
import art.ameliah.hsr.lightcones.abundance.PostOpConversation;
import art.ameliah.hsr.lightcones.harmony.ForTomorrowsJourney;
import art.ameliah.hsr.lightcones.hunt.SleepLikeTheDead;
import art.ameliah.hsr.relics.RelicStats;
import art.ameliah.hsr.relics.Stats;
import art.ameliah.hsr.relics.ornament.DuranDynastyOfRunningWolves;
import art.ameliah.hsr.relics.ornament.FleetOfTheAgeless;
import art.ameliah.hsr.relics.ornament.InertSalsotto;
import art.ameliah.hsr.relics.ornament.LushakaTheSunkenSeas;
import art.ameliah.hsr.relics.relics.MessengerTraversingHackerspace;
import art.ameliah.hsr.relics.relics.PasserbyOfWanderingCloud;
import art.ameliah.hsr.relics.relics.PrisonerInDeepConfinement;
import art.ameliah.hsr.relics.relics.TheAshblazingGrandDuke;
import art.ameliah.hsr.relics.relics.TheWindSoaringValorous;

import java.util.List;

public class Divteams {

    public static List<AbstractCharacter<?>> players() {
        return List.of(divsHuoHuo(), divsFeixiao(), divsRobin(), divsTopaz());
    }

    public static AbstractCharacter<?> divsHuoHuo() {
        Huohuo huohuo = new Huohuo();
        PostOpConversation lc = new PostOpConversation(huohuo);

        huohuo.EquipLightcone(lc);
        huohuo.EquipRelicSet(new MessengerTraversingHackerspace(huohuo));
        huohuo.EquipRelicSet(new PasserbyOfWanderingCloud(huohuo));
        huohuo.EquipRelicSet(new FleetOfTheAgeless(huohuo));

        new RelicStats()
                .addMainStat(Stats.HEALING)
                .addMainStat(Stats.SPEED)
                .addMainStat(Stats.HP_PER)
                .addMainStat(Stats.ERR)
                .addSubStat(Stats.HP_PER, 7)
                .addSubStat(Stats.SPEED, 5)
                .equipTo(huohuo);

        return huohuo;
    }

    public static AbstractCharacter<?> divsRobin() {
        Robin robin = new Robin();
        ForTomorrowsJourney lc = new ForTomorrowsJourney(robin);

        robin.EquipLightcone(lc);
        robin.EquipRelicSet(new PrisonerInDeepConfinement(robin));
        robin.EquipRelicSet(new MessengerTraversingHackerspace(robin));
        robin.EquipRelicSet(new LushakaTheSunkenSeas(robin));

        new RelicStats()
                .addMainStat(Stats.ATK_PER)
                .addMainStat(Stats.ATK_PER)
                .addMainStat(Stats.ATK_PER)
                .addMainStat(Stats.ERR)
                .addSubStat(Stats.ATK_PER, 6)
                .addSubStat(Stats.SPEED, 8)
                .equipTo(robin);

        return robin;
    }

    public static AbstractCharacter<?> divsTopaz() {
        Topaz topaz = new Topaz();
        SleepLikeTheDead lc = new SleepLikeTheDead(topaz);

        topaz.EquipLightcone(lc);
        topaz.EquipRelicSet(new TheAshblazingGrandDuke(topaz, true));
        topaz.EquipRelicSet(new InertSalsotto(topaz));

        new RelicStats()
                .addMainStat(Stats.CRIT_RATE)
                .addMainStat(Stats.SPEED)
                .addMainStat(Stats.FIRE_DAMAGE)
                .addMainStat(Stats.ATK_PER)
                .addSubStat(Stats.CRIT_RATE, 15)
                .addSubStat(Stats.CRIT_DAMAGE, 13)
                .addSubStat(Stats.ATK_PER, 7)
                .addSubStat(Stats.SPEED, 3)
                .equipTo(topaz);

        return topaz;
    }

    public static AbstractCharacter<?> divsFeixiao() {
        Feixiao feixiao = new Feixiao();
        SleepLikeTheDead lc = new SleepLikeTheDead(feixiao);

        feixiao.EquipLightcone(lc);
        feixiao.EquipRelicSet(new TheWindSoaringValorous(feixiao, true));
        feixiao.EquipRelicSet(new DuranDynastyOfRunningWolves(feixiao));

        RelicStats relicStats = new RelicStats();
        relicStats.addMainStat(Stats.CRIT_RATE)
                .addMainStat(Stats.SPEED)
                .addMainStat(Stats.WIND_DAMAGE)
                .addMainStat(Stats.ATK_PER)
                .addSubStat(Stats.CRIT_RATE, 15)
                .addSubStat(Stats.CRIT_DAMAGE, 12)
                .addSubStat(Stats.ATK_PER, 6);

        relicStats.equipTo(feixiao);

        return feixiao;
    }

}
