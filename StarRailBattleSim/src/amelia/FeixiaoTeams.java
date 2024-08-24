package amelia;

import characters.AbstractCharacter;
import characters.bronya.Bronya;
import characters.feixiao.Feixiao;
import characters.gallagher.Gallagher;
import characters.goal.shared.ForceAdvanceGoal;
import characters.huohuo.Huohuo;
import characters.march.SwordMarch;
import characters.robin.Robin;
import characters.ruanmei.RuanMei;
import lightcones.AbstractLightcone;
import lightcones.abundance.NightOfFright;
import lightcones.abundance.QuidProQuo;
import lightcones.harmony.ButTheBattleIsntOver;
import lightcones.harmony.FlowingNightglow;
import lightcones.hunt.CruisingInTheStellarSea;
import lightcones.hunt.IVentureForthToHunt;
import relics.RelicStats;
import relics.ornament.BrokenKeel;
import relics.ornament.DuranDynastyOfRunningWolves;
import relics.ornament.FleetOfTheAgeless;
import relics.ornament.ForgeOfTheKalpagniLatern;
import relics.ornament.LushakaTheSunkenSeas;
import relics.ornament.PenaconyLandOfTheDreams;
import relics.ornament.RutilentArena;
import relics.ornament.SpringhtlyVonwacq;
import relics.relics.LongevousDisciple;
import relics.relics.MessengerTraversingHackerspace;
import relics.relics.PasserbyOfWanderingCloud;
import relics.relics.PrisonerInDeepConfinement;
import relics.relics.TheWindSoaringValorous;
import relics.relics.ThiefOfShootingMeteor;
import relics.relics.WatchMakerMasterOfDreamMachinations;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FeixiaoTeams {

    public static List<AbstractCharacter<?>> FeixiaoMarchRobinAnyGallaghger(LightConeSupplier lightconeSupplier) {
        return FeixiaoMarch(
                () -> myRobin(lightconeSupplier),
                FeixiaoTeams::myGallagher
        );
    }

    public static List<AbstractCharacter<?>> FeixiaoMarchSupportAnyGallaghger(Supplier<AbstractCharacter<?>> supportSupplier) {
        return FeixiaoMarch(supportSupplier, FeixiaoTeams::myGallagher);
    }

    public static List<AbstractCharacter<?>> FeixiaoMarch(Supplier<AbstractCharacter<?>> supportSupplier, Supplier<AbstractCharacter<?>> fourthSupplier) {
        List<AbstractCharacter<?>> team = new ArrayList<>();
        team.add(myFeixiao());
        team.add(myMarch());
        team.add(supportSupplier.get());
        team.add(fourthSupplier.get());

        return team;
    }

    static AbstractCharacter<?> myBroyna() {
        return myBroyna(ButTheBattleIsntOver::new, 5);
    }

    static AbstractCharacter<?> myBroyna(LightConeSupplier lightconeSupplier) {
        return myBroyna(lightconeSupplier, 5);
    }

    static AbstractCharacter<?> myBroyna(LightConeSupplier lightconeSupplier, int speed) {
        Bronya broyna = new Bronya();
        broyna.EquipLightcone(lightconeSupplier.get(broyna));
        broyna.EquipRelicSet(new LongevousDisciple(broyna));
        broyna.EquipRelicSet(new BrokenKeel(broyna));

        RelicStats stats = new RelicStats();
        stats.addMainStat(RelicStats.Stats.CRIT_DAMAGE)
                .addMainStat(RelicStats.Stats.SPEED)
                .addMainStat(RelicStats.Stats.HP_PER)
                .addMainStat(RelicStats.Stats.ERR)
                .addSubStat(RelicStats.Stats.CRIT_DAMAGE, 10)
                .addSubStat(RelicStats.Stats.SPEED, speed)
                .addSubStat(RelicStats.Stats.EFFECT_RES, 5);

        stats.equipTo(broyna);

        // DMG goes wayyy down if we force advance
        //broyna.registerGoal(-10, new ForceAdvanceGoal<>(broyna, Robin.NAME));
        return broyna;
    }

    static AbstractCharacter<?> myRuanMei(LightConeSupplier lightconeSupplier) {
        AbstractCharacter<?> ruanMei = new RuanMei();
        ruanMei.EquipLightcone(lightconeSupplier.get(ruanMei));
        ruanMei.EquipRelicSet(new ThiefOfShootingMeteor(ruanMei, false));
        ruanMei.EquipRelicSet(new WatchMakerMasterOfDreamMachinations(ruanMei, false));
        ruanMei.EquipRelicSet(new PenaconyLandOfTheDreams(ruanMei));

        RelicStats stats = new RelicStats();
        stats.addMainStat(RelicStats.Stats.DEF_PER)
                .addMainStat(RelicStats.Stats.SPEED)
                .addMainStat(RelicStats.Stats.HP_PER)
                .addMainStat(RelicStats.Stats.ERR)
                .addSubStat(RelicStats.Stats.BREAK_EFFECT, 13)
                .addSubStat(RelicStats.Stats.SPEED, 8);

        stats.equipTo(ruanMei);
        return ruanMei;
    }

    static AbstractCharacter<?> myRobin() {
        return myRobin(FlowingNightglow::new);
    }

    static AbstractCharacter<?> myRobin(LightConeSupplier lightconeSupplier) {
        AbstractCharacter<?> robin = new Robin();
        robin.EquipLightcone(lightconeSupplier.get(robin));
        robin.EquipRelicSet(new PrisonerInDeepConfinement(robin, false));
        robin.EquipRelicSet(new TheWindSoaringValorous(robin, false));
        robin.EquipRelicSet(new LushakaTheSunkenSeas(robin));

        RelicStats stats = new RelicStats();
        stats.addMainStat(RelicStats.Stats.ATK_PER)
                .addMainStat(RelicStats.Stats.ATK_PER).
                addMainStat(RelicStats.Stats.ATK_PER)
                .addMainStat(RelicStats.Stats.ERR)
                .addSubStat(RelicStats.Stats.ATK_PER, 7)
                //.addSubStat(RelicStats.Stats.SPEED, 8)
                .addSubStat(RelicStats.Stats.ATK_FLAT, 3)
                .addSubStat(RelicStats.Stats.EFFECT_RES, 6);

        stats.equipTo(robin);
        return robin;
    }

    static AbstractCharacter<?> myGallagher() {
        return myGallagher(QuidProQuo::new);
    }

    static AbstractCharacter<?> myGallagher(LightConeSupplier lightconeSupplier) {
        AbstractCharacter<?> gallagher = new Gallagher();
        gallagher.EquipLightcone(lightconeSupplier.get(gallagher));
        gallagher.EquipRelicSet(new WatchMakerMasterOfDreamMachinations(gallagher, false));
        gallagher.EquipRelicSet(new MessengerTraversingHackerspace(gallagher, false));
        gallagher.EquipRelicSet(new ForgeOfTheKalpagniLatern(gallagher));

        RelicStats stats = new RelicStats();
        stats.addMainStat(RelicStats.Stats.HEALING)
                .addMainStat(RelicStats.Stats.SPEED)
                .addMainStat(RelicStats.Stats.HP_PER)
                .addMainStat(RelicStats.Stats.BREAK_EFFECT)
                .addSubStat(RelicStats.Stats.BREAK_EFFECT, 6)
                .addSubStat(RelicStats.Stats.SPEED, 7);

        stats.equipTo(gallagher);
        return gallagher;
    }

    static AbstractCharacter<?> myMarch() {
        return myMarch(CruisingInTheStellarSea::new);
    }

    static AbstractCharacter<?> myMarch(LightConeSupplier lightConeSupplier) {
        AbstractCharacter<?> march = new SwordMarch();
        march.EquipLightcone(lightConeSupplier.get(march));
        march.EquipRelicSet(new PrisonerInDeepConfinement(march, false));
        march.EquipRelicSet(new MessengerTraversingHackerspace(march, false));
        march.EquipRelicSet(new RutilentArena(march));

        RelicStats stats = new RelicStats();
        stats.addMainStat(RelicStats.Stats.CRIT_RATE)
                .addMainStat(RelicStats.Stats.SPEED)
                .addMainStat(RelicStats.Stats.ELEMENT_DAMAGE)
                .addMainStat(RelicStats.Stats.ATK_PER)
                .addSubStat(RelicStats.Stats.CRIT_RATE, 7)
                .addSubStat(RelicStats.Stats.CRIT_DAMAGE, 11)
                .addSubStat(RelicStats.Stats.ATK_PER, 6)
                .addSubStat(RelicStats.Stats.SPEED, 2);

        stats.equipTo(march);
        return march;
    }

    static AbstractCharacter<?> myFeixiao() {
        return myFeixiao(IVentureForthToHunt::new);
    }

    static AbstractCharacter<?> myFeixiao(LightConeSupplier lightConeSupplier) {
        AbstractCharacter<?> feixiao = new Feixiao();
        feixiao.EquipLightcone(lightConeSupplier.get(feixiao));
        feixiao.EquipRelicSet(new TheWindSoaringValorous(feixiao));
        feixiao.EquipRelicSet(new DuranDynastyOfRunningWolves(feixiao));

        RelicStats stats = new RelicStats();
        stats.addMainStat(RelicStats.Stats.CRIT_RATE)
                .addMainStat(RelicStats.Stats.SPEED)
                .addMainStat(RelicStats.Stats.ATK_PER)
                .addMainStat(RelicStats.Stats.ATK_PER)
                .addSubStat(RelicStats.Stats.CRIT_RATE, 11)
                .addSubStat(RelicStats.Stats.CRIT_DAMAGE, 16)
                .addSubStat(RelicStats.Stats.ATK_PER, 2)
                .addSubStat(RelicStats.Stats.SPEED, 1);

        stats.equipTo(feixiao);
        return feixiao;
    }

    static AbstractCharacter<?> myHuoHuo() {
        return myHuoHuo(NightOfFright::new);
    }

    static AbstractCharacter<?> myHuoHuo(LightConeSupplier lightconeSupplier) {
        AbstractCharacter<?> huohuo = new Huohuo();
        huohuo.EquipLightcone(lightconeSupplier.get(huohuo));
        huohuo.EquipRelicSet(new PasserbyOfWanderingCloud(huohuo, false));
        huohuo.EquipRelicSet(new MessengerTraversingHackerspace(huohuo, false));
        huohuo.EquipRelicSet(new FleetOfTheAgeless(huohuo));

        RelicStats stats = new RelicStats();
        stats.addMainStat(RelicStats.Stats.HP_PER)
                .addMainStat(RelicStats.Stats.SPEED)
                .addMainStat(RelicStats.Stats.HP_PER)
                .addMainStat(RelicStats.Stats.ERR);

        stats.equipTo(huohuo);
        return huohuo;
    }
    
    @FunctionalInterface
    public interface LightConeSupplier {
        AbstractLightcone get(AbstractCharacter<?> character);
    }

}