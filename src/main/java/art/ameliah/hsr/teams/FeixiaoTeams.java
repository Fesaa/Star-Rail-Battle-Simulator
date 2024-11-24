package art.ameliah.hsr.teams;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.aventurine.Aventurine;
import art.ameliah.hsr.characters.bronya.Bronya;
import art.ameliah.hsr.characters.feixiao.Feixiao;
import art.ameliah.hsr.characters.gallagher.Gallagher;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysBasicGoal;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysSkillGoal;
import art.ameliah.hsr.characters.huohuo.Huohuo;
import art.ameliah.hsr.characters.march.SwordMarch;
import art.ameliah.hsr.characters.moze.Moze;
import art.ameliah.hsr.characters.robin.Robin;
import art.ameliah.hsr.characters.ruanmei.RuanMei;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.lightcones.abundance.NightOfFright;
import art.ameliah.hsr.lightcones.abundance.QuidProQuo;
import art.ameliah.hsr.lightcones.harmony.ButTheBattleIsntOver;
import art.ameliah.hsr.lightcones.harmony.FlowingNightglow;
import art.ameliah.hsr.lightcones.hunt.CruisingInTheStellarSea;
import art.ameliah.hsr.lightcones.hunt.IVentureForthToHunt;
import art.ameliah.hsr.lightcones.hunt.Swordplay;
import art.ameliah.hsr.lightcones.preservation.ConcertForTwo;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;
import art.ameliah.hsr.relics.RelicStats;
import art.ameliah.hsr.relics.Stats;
import art.ameliah.hsr.relics.ornament.BrokenKeel;
import art.ameliah.hsr.relics.ornament.DuranDynastyOfRunningWolves;
import art.ameliah.hsr.relics.ornament.FleetOfTheAgeless;
import art.ameliah.hsr.relics.ornament.ForgeOfTheKalpagniLatern;
import art.ameliah.hsr.relics.ornament.PenaconyLandOfTheDreams;
import art.ameliah.hsr.relics.ornament.RutilentArena;
import art.ameliah.hsr.relics.ornament.SpringhtlyVonwacq;
import art.ameliah.hsr.relics.relics.KnightOfPurityPalace;
import art.ameliah.hsr.relics.relics.LongevousDisciple;
import art.ameliah.hsr.relics.relics.MessengerTraversingHackerspace;
import art.ameliah.hsr.relics.relics.PasserbyOfWanderingCloud;
import art.ameliah.hsr.relics.relics.PrisonerInDeepConfinement;
import art.ameliah.hsr.relics.relics.TheWindSoaringValorous;
import art.ameliah.hsr.relics.relics.ThiefOfShootingMeteor;
import art.ameliah.hsr.relics.relics.WatchMakerMasterOfDreamMachinations;

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

    public static AbstractCharacter<?> myBroyna() {
        return myBroyna(ButTheBattleIsntOver::new, 5);
    }

    public static AbstractCharacter<?> myBroyna(LightConeSupplier lightconeSupplier) {
        return myBroyna(lightconeSupplier, 5);
    }

    public static AbstractCharacter<?> myBroyna(LightConeSupplier lightconeSupplier, int speed) {
        Bronya broyna = new Bronya();
        broyna.EquipLightcone(lightconeSupplier.get(broyna));
        broyna.EquipRelicSet(new LongevousDisciple(broyna));
        broyna.EquipRelicSet(new BrokenKeel(broyna));

        RelicStats stats = new RelicStats();
        stats.addMainStat(Stats.CRIT_DAMAGE)
                .addMainStat(Stats.SPEED)
                .addMainStat(Stats.HP_PER)
                .addMainStat(Stats.ERR)
                .addSubStat(Stats.CRIT_DAMAGE, 10)
                .addSubStat(Stats.SPEED, speed)
                .addSubStat(Stats.EFFECT_RES, 5);

        stats.equipTo(broyna);

        // DMG goes wayyy down if we force advance
        //broyna.registerGoal(-10, new ForceAdvanceGoal<>(broyna, Robin.NAME));
        return broyna;
    }

    public static AbstractCharacter<?> myRuanMei(LightConeSupplier lightconeSupplier) {
        AbstractCharacter<?> ruanMei = new RuanMei();
        ruanMei.EquipLightcone(lightconeSupplier.get(ruanMei));
        ruanMei.EquipRelicSet(new ThiefOfShootingMeteor(ruanMei, false));
        ruanMei.EquipRelicSet(new WatchMakerMasterOfDreamMachinations(ruanMei, false));
        ruanMei.EquipRelicSet(new PenaconyLandOfTheDreams(ruanMei));

        RelicStats stats = new RelicStats();
        stats.addMainStat(Stats.DEF_PER)
                .addMainStat(Stats.SPEED)
                .addMainStat(Stats.HP_PER)
                .addMainStat(Stats.ERR)
                .addSubStat(Stats.BREAK_EFFECT, 13)
                .addSubStat(Stats.SPEED, 8);

        stats.equipTo(ruanMei);
        return ruanMei;
    }

    public static AbstractCharacter<?> robinSpd(LightConeSupplier lightconeSupplier) {
        Robin robin = new Robin();
        robin.EquipLightcone(lightconeSupplier.get(robin));
        robin.EquipRelicSet(new PrisonerInDeepConfinement(robin, false));
        robin.EquipRelicSet(new TheWindSoaringValorous(robin, false));
        robin.EquipRelicSet(new SpringhtlyVonwacq(robin));

        RelicStats stats = new RelicStats();
        stats.addMainStat(Stats.ATK_PER)
                .addMainStat(Stats.SPEED)
                .addMainStat(Stats.ATK_PER)
                .addMainStat(Stats.ERR)
                .addSubStat(Stats.ATK_PER, 4)
                .addSubStat(Stats.SPEED, 12)
                .addSubStat(Stats.ATK_FLAT, 3)
                .addSubStat(Stats.EFFECT_RES, 6);

        stats.equipTo(robin);
        return robin;
    }

    public static AbstractCharacter<?> myRobin(boolean e1) {
        return myRobin((AbstractCharacter<?> c) -> new SpringhtlyVonwacq(c), e1);
    }

    public static AbstractCharacter<?> myRobin(RelicSupplier relicSupplier, boolean e1) {
        return myRobin(FlowingNightglow::new, relicSupplier, e1);
    }

    public static AbstractCharacter<?> myRobin(LightConeSupplier lightconeSupplier, boolean e1) {
        return myRobin(lightconeSupplier, SpringhtlyVonwacq::new, e1);
    }

    public static AbstractCharacter<?> myRobin() {
        return myRobin(FlowingNightglow::new);
    }

    public static AbstractCharacter<?> myRobin(LightConeSupplier lightconeSupplier) {
        return myRobin(lightconeSupplier, SpringhtlyVonwacq::new);
    }

    public static AbstractCharacter<?> myRobin(LightConeSupplier lightconeSupplier, RelicSupplier relicSupplier) {
        return myRobin(lightconeSupplier, relicSupplier, false);
    }

    public static AbstractCharacter<?> myRobin(LightConeSupplier lightconeSupplier, RelicSupplier relicSupplier, boolean e1) {
        AbstractCharacter<?> robin = new Robin();

        AbstractRelicSetBonus relic = relicSupplier.get(robin);
        robin.EquipLightcone(lightconeSupplier.get(robin));
        robin.EquipRelicSet(new PrisonerInDeepConfinement(robin, false));
        robin.EquipRelicSet(new TheWindSoaringValorous(robin, false));
        robin.EquipRelicSet(relic);

        RelicStats stats = new RelicStats();
        stats.addMainStat(Stats.ATK_PER)
                .addMainStat(Stats.ATK_PER).
                addMainStat(Stats.ATK_PER)
                .addMainStat(Stats.ERR)
                .addSubStat(Stats.ATK_PER, 2)
                .addSubStat(Stats.SPEED, 8)
                .addSubStat(Stats.ATK_FLAT, 3)
                .addSubStat(Stats.EFFECT_RES, 6);

        stats.equipTo(robin);
        return robin;
    }

    public static AbstractCharacter<?> myGallagher() {
        return myGallagher(QuidProQuo::new);
    }

    public static AbstractCharacter<?> myGallagher(LightConeSupplier lightconeSupplier) {
        AbstractCharacter<?> gallagher = new Gallagher();
        gallagher.EquipLightcone(lightconeSupplier.get(gallagher));
        gallagher.EquipRelicSet(new WatchMakerMasterOfDreamMachinations(gallagher, false));
        gallagher.EquipRelicSet(new MessengerTraversingHackerspace(gallagher, false));
        gallagher.EquipRelicSet(new ForgeOfTheKalpagniLatern(gallagher));

        RelicStats stats = new RelicStats();
        stats.addMainStat(Stats.HEALING)
                .addMainStat(Stats.SPEED)
                .addMainStat(Stats.HP_PER)
                .addMainStat(Stats.BREAK_EFFECT)
                .addSubStat(Stats.BREAK_EFFECT, 6)
                .addSubStat(Stats.SPEED, 7);

        stats.equipTo(gallagher);
        return gallagher;
    }

    public static AbstractCharacter<?> myMarch() {
        return myMarch(CruisingInTheStellarSea::new);
    }

    public static AbstractCharacter<?> myMarch(LightConeSupplier lightConeSupplier) {
        AbstractCharacter<?> march = new SwordMarch();
        march.EquipLightcone(lightConeSupplier.get(march));
        // march.EquipRelicSet(new PrisonerInDeepConfinement(march, false));
        //march.EquipRelicSet(new WatchMakerMasterOfDreamMachinations(march, false));
        march.EquipRelicSet(new RutilentArena(march));

        RelicStats stats = new RelicStats();
        stats.addMainStat(Stats.CRIT_DAMAGE)
                .addMainStat(Stats.SPEED)
                .addMainStat(Stats.IMAGINARY_DAMAGE)
                .addMainStat(Stats.ATK_PER)
                .addSubStat(Stats.CRIT_RATE, 17) // 3 + 2 + 3 + 3 + 3 + 3
                .addSubStat(Stats.CRIT_DAMAGE, 2) // 1 + 2 + 2
                .addSubStat(Stats.ATK_PER, 3) // 1 + 2
                .addSubStat(Stats.SPEED, 7); // 1 + 3 + 2
        stats.equipTo(march);
        return march;
    }

    public static AbstractCharacter<?> myOtherMarch() {
        return myOtherMarch(CruisingInTheStellarSea::new);
    }

    public static AbstractCharacter<?> myOtherMarch(LightConeSupplier lightConeSupplier) {
        AbstractCharacter<?> march = new SwordMarch();
        march.EquipLightcone(lightConeSupplier.get(march));
        march.EquipRelicSet(new MessengerTraversingHackerspace(march, false));
        march.EquipRelicSet(new RutilentArena(march));

        RelicStats stats = new RelicStats();
        stats.addMainStat(Stats.CRIT_DAMAGE)
                .addMainStat(Stats.SPEED)
                .addMainStat(Stats.IMAGINARY_DAMAGE)
                .addMainStat(Stats.ATK_PER)
                .addSubStat(Stats.CRIT_RATE, 15) // 3 + 1 + 2 + 3 + 3 + 3
                .addSubStat(Stats.CRIT_DAMAGE, 3) // 1 + 2
                .addSubStat(Stats.ATK_PER, 4) // 2 + 2 +
                .addSubStat(Stats.SPEED, 6); // 2 + 3 + 1

        stats.equipTo(march);
        return march;
    }

    public static AbstractCharacter<?> myMoze() {
        return myMoze(Swordplay::new);
    }

    public static AbstractCharacter<?> myMoze(LightConeSupplier lightConeSupplier) {
        AbstractCharacter<?> moze = new Moze();
        moze.EquipLightcone(lightConeSupplier.get(moze));
        moze.EquipRelicSet(new PrisonerInDeepConfinement(moze, false));
        moze.EquipRelicSet(new MessengerTraversingHackerspace(moze, false));
        moze.EquipRelicSet(new RutilentArena(moze));

        RelicStats stats = new RelicStats();
        stats.addMainStat(Stats.CRIT_RATE)
                .addMainStat(Stats.SPEED)
                .addMainStat(Stats.LIGHTNING_DAMAGE)
                .addMainStat(Stats.ATK_PER)
                .addSubStat(Stats.CRIT_RATE, 7)
                .addSubStat(Stats.CRIT_DAMAGE, 11)
                .addSubStat(Stats.ATK_PER, 6)
                .addSubStat(Stats.SPEED, 2);

        stats.equipTo(moze);
        return moze;
    }

    public static AbstractCharacter<?> myFeixiao() {
        return myFeixiao(IVentureForthToHunt::new);
    }

    public static AbstractCharacter<?> myFeixiaoCD(LightConeSupplier lightConeSupplier) {
        return myFeixiaoCD(lightConeSupplier, false);
    }

    public static AbstractCharacter<?> myFeixiaoCD(LightConeSupplier lightConeSupplier, boolean aggressive) {
        Feixiao feixiao = new Feixiao();
        feixiao.EquipLightcone(lightConeSupplier.get(feixiao));
        feixiao.EquipRelicSet(new TheWindSoaringValorous(feixiao));
        feixiao.EquipRelicSet(new DuranDynastyOfRunningWolves(feixiao));

        RelicStats stats = new RelicStats();
        stats.addMainStat(Stats.CRIT_DAMAGE)
                .addMainStat(Stats.SPEED)
                .addMainStat(Stats.ATK_PER)
                .addMainStat(Stats.ATK_PER)
                .addSubStat(Stats.CRIT_RATE, 14)
                .addSubStat(Stats.CRIT_DAMAGE, 12)
                .addSubStat(Stats.ATK_PER, 2)
                .addSubStat(Stats.SPEED, 4);

        stats.equipTo(feixiao);

        if (aggressive) {
            feixiao.clearTurnGoals();
            //feixiao.registerGoal(0, new FeixiaoBronyaTurnGoal(feixiao));
            feixiao.registerGoal(10, new AlwaysSkillGoal<>(feixiao, 1));
        }

        return feixiao;
    }

    public static AbstractCharacter<?> myFeixiao(LightConeSupplier lightConeSupplier) {
        return myFeixiao(lightConeSupplier, false);
    }

    public static AbstractCharacter<?> myFeixiao(LightConeSupplier lightConeSupplier, boolean aggressive) {
        Feixiao feixiao = new Feixiao();
        feixiao.EquipLightcone(lightConeSupplier.get(feixiao));
        feixiao.EquipRelicSet(new TheWindSoaringValorous(feixiao));
        feixiao.EquipRelicSet(new DuranDynastyOfRunningWolves(feixiao));

        RelicStats stats = new RelicStats();
        stats.addMainStat(Stats.CRIT_RATE)
                .addMainStat(Stats.SPEED)
                .addMainStat(Stats.ATK_PER)
                .addMainStat(Stats.ATK_PER)
                .addSubStat(Stats.CRIT_RATE, 11)
                .addSubStat(Stats.CRIT_DAMAGE, 16)
                .addSubStat(Stats.ATK_PER, 2)
                .addSubStat(Stats.SPEED, 1);

        stats.equipTo(feixiao);

        if (aggressive) {
            feixiao.clearTurnGoals();
            //feixiao.registerGoal(0, new FeixiaoBronyaTurnGoal(feixiao));
            feixiao.registerGoal(10, new AlwaysSkillGoal<>(feixiao, 1));
        }

        return feixiao;
    }

    public static AbstractCharacter<?> myHuoHuo() {
        return myHuoHuo(NightOfFright::new);
    }

    public static AbstractCharacter<?> myHuoHuo(LightConeSupplier lightconeSupplier) {
        AbstractCharacter<?> huohuo = new Huohuo();
        huohuo.EquipLightcone(lightconeSupplier.get(huohuo));
        huohuo.EquipRelicSet(new PasserbyOfWanderingCloud(huohuo, false));
        huohuo.EquipRelicSet(new MessengerTraversingHackerspace(huohuo, false));
        huohuo.EquipRelicSet(new FleetOfTheAgeless(huohuo));

        RelicStats stats = new RelicStats();
        stats.addMainStat(Stats.HP_PER)
                .addMainStat(Stats.SPEED)
                .addMainStat(Stats.HP_PER)
                .addMainStat(Stats.ERR);

        stats.equipTo(huohuo);
        return huohuo;
    }

    public static AbstractCharacter<?> myAventurine(Stats bodyStat) {
        return myAventurine(ConcertForTwo::new, bodyStat);
    }

    public static AbstractCharacter<?> myAventurine(LightConeSupplier lightConeSupplier, Stats bodyStat) {
        Aventurine aventurine = new Aventurine();
        aventurine.EquipLightcone(lightConeSupplier.get(aventurine));
        aventurine.EquipRelicSet(new KnightOfPurityPalace(aventurine, true));
        aventurine.EquipRelicSet(new BrokenKeel(aventurine));

        RelicStats stats = new RelicStats();
        stats.addMainStat(bodyStat)
                .addMainStat(Stats.SPEED)
                .addMainStat(Stats.DEF_PER)
                .addMainStat(Stats.DEF_PER)
                .addSubStat(Stats.DEF_PER, 7)
                .addSubStat(Stats.SPEED, 2)
                .addSubStat(Stats.CRIT_DAMAGE, 13)
                .addSubStat(Stats.DEF_FLAT, 6);

        stats.equipTo(aventurine);
        aventurine.clearTurnGoals();
        aventurine.registerGoal(0, new AlwaysBasicGoal<>(aventurine));

        return aventurine;
    }

    @FunctionalInterface
    public interface LightConeSupplier {
        AbstractLightcone get(AbstractCharacter<?> character);
    }

    @FunctionalInterface
    public interface RelicSupplier {
        AbstractRelicSetBonus get(AbstractCharacter<?> character);
    }

}