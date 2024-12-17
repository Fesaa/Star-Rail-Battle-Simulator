package art.ameliah.hsr.registry;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;
import art.ameliah.hsr.relics.ornament.BelobogOfTheArchitects;
import art.ameliah.hsr.relics.ornament.BrokenKeel;
import art.ameliah.hsr.relics.ornament.CelestialDifferentiator;
import art.ameliah.hsr.relics.ornament.DuranDynastyOfRunningWolves;
import art.ameliah.hsr.relics.ornament.FirmamentFrontlineGlamoth;
import art.ameliah.hsr.relics.ornament.FleetOfTheAgeless;
import art.ameliah.hsr.relics.ornament.ForgeOfTheKalpagniLatern;
import art.ameliah.hsr.relics.ornament.InertSalsotto;
import art.ameliah.hsr.relics.ornament.IzumoGenseiAndTakamaDivineRealm;
import art.ameliah.hsr.relics.ornament.LushakaTheSunkenSeas;
import art.ameliah.hsr.relics.ornament.PanCosmicCommercialEnterprise;
import art.ameliah.hsr.relics.ornament.PenaconyLandOfTheDreams;
import art.ameliah.hsr.relics.ornament.RutilentArena;
import art.ameliah.hsr.relics.ornament.SigoniaTheUnclaimedDesolation;
import art.ameliah.hsr.relics.ornament.SpaceSealingStation;
import art.ameliah.hsr.relics.ornament.SpringhtlyVonwacq;
import art.ameliah.hsr.relics.ornament.TaliaKingdomOfBanditry;
import art.ameliah.hsr.relics.ornament.TheWondrousBananAmusementPark;
import art.ameliah.hsr.relics.relics.BandOfSizzlingThunder;
import art.ameliah.hsr.relics.relics.ChampionOfStreetwiseBoxing;
import art.ameliah.hsr.relics.relics.EagleOfTwilightLine;
import art.ameliah.hsr.relics.relics.FiresmithOfLavaForging;
import art.ameliah.hsr.relics.relics.GeniusOfBrilliantStars;
import art.ameliah.hsr.relics.relics.GuardInTheWutheringSnow;
import art.ameliah.hsr.relics.relics.HunterOfTheGlacialForest;
import art.ameliah.hsr.relics.relics.IronCavalryAgainstTheScourge;
import art.ameliah.hsr.relics.relics.KnightOfPurityPalace;
import art.ameliah.hsr.relics.relics.LongevousDisciple;
import art.ameliah.hsr.relics.relics.MessengerTraversingHackerspace;
import art.ameliah.hsr.relics.relics.MusketeerOfWildWheat;
import art.ameliah.hsr.relics.relics.PasserbyOfWanderingCloud;
import art.ameliah.hsr.relics.relics.PioneerDiverOfDeadWaters;
import art.ameliah.hsr.relics.relics.PoetOfMourningCollapse;
import art.ameliah.hsr.relics.relics.PrisonerInDeepConfinement;
import art.ameliah.hsr.relics.relics.SacerdosRelivedOrdeal;
import art.ameliah.hsr.relics.relics.ScholarLostInErudition;
import art.ameliah.hsr.relics.relics.TheAshblazingGrandDuke;
import art.ameliah.hsr.relics.relics.TheWindSoaringValorous;
import art.ameliah.hsr.relics.relics.ThiefOfShootingMeteor;
import art.ameliah.hsr.relics.relics.WastelandOfBanditryDesert;
import art.ameliah.hsr.relics.relics.WatchMakerMasterOfDreamMachinations;

public class RelicSetRegistry extends AbstractRegistry<AbstractRelicSetBonus> {

    public static final RelicSetRegistry INSTANCE = new RelicSetRegistry();

    public RelicSetRegistry() {
        register(101, PasserbyOfWanderingCloud.class);
        register(102, MusketeerOfWildWheat.class);
        register(103, KnightOfPurityPalace.class);
        register(104, HunterOfTheGlacialForest.class);
        register(105, ChampionOfStreetwiseBoxing.class);
        register(106, GuardInTheWutheringSnow.class);
        register(107, FiresmithOfLavaForging.class);
        register(108, GeniusOfBrilliantStars.class);
        register(109, BandOfSizzlingThunder.class);
        register(110, EagleOfTwilightLine.class);
        register(111, ThiefOfShootingMeteor.class);
        register(112, WastelandOfBanditryDesert.class);
        register(113, LongevousDisciple.class);
        register(114, MessengerTraversingHackerspace.class);
        register(115, TheAshblazingGrandDuke.class);
        register(116, PrisonerInDeepConfinement.class);
        register(117, PioneerDiverOfDeadWaters.class);
        register(118, WatchMakerMasterOfDreamMachinations.class);
        register(119, IronCavalryAgainstTheScourge.class);
        register(120, TheWindSoaringValorous.class);
        register(121, SacerdosRelivedOrdeal.class);
        register(122, ScholarLostInErudition.class);
        register(124, PoetOfMourningCollapse.class);


        register(301, SpaceSealingStation.class);
        register(302, FleetOfTheAgeless.class);
        register(303, PanCosmicCommercialEnterprise.class);
        register(304, BelobogOfTheArchitects.class);
        register(305, CelestialDifferentiator.class);
        register(306, InertSalsotto.class);
        register(307, TaliaKingdomOfBanditry.class);
        register(308, SpringhtlyVonwacq.class);
        register(309, RutilentArena.class);
        register(310, BrokenKeel.class);
        register(311, FirmamentFrontlineGlamoth.class);
        register(312, PenaconyLandOfTheDreams.class);
        register(313, SigoniaTheUnclaimedDesolation.class);
        register(314, IzumoGenseiAndTakamaDivineRealm.class);
        register(315, DuranDynastyOfRunningWolves.class);
        register(316, ForgeOfTheKalpagniLatern.class);
        register(317, LushakaTheSunkenSeas.class);
        register(318, TheWondrousBananAmusementPark.class);
    }

    public AbstractRelicSetBonus get(int id, AbstractCharacter<?> owner, boolean fullSet) throws Exception {
        if (registry.containsKey(id)) {
            return registry.get(id).getConstructor(AbstractCharacter.class, boolean.class).newInstance(owner, fullSet);
        }
        throw new RuntimeException("Element with id" + id + " not found");
    }

}
