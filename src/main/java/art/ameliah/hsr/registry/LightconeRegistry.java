package art.ameliah.hsr.registry;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.lightcones.abundance.EchoesOftheCoffin;
import art.ameliah.hsr.lightcones.abundance.HeyOverHere;
import art.ameliah.hsr.lightcones.abundance.NightOfFright;
import art.ameliah.hsr.lightcones.abundance.PerfectTiming;
import art.ameliah.hsr.lightcones.abundance.PostOpConversation;
import art.ameliah.hsr.lightcones.abundance.QuidProQuo;
import art.ameliah.hsr.lightcones.abundance.ScentAloneStaysTrue;
import art.ameliah.hsr.lightcones.abundance.SharedFeeling;
import art.ameliah.hsr.lightcones.abundance.TimeWaitsForNoOne;
import art.ameliah.hsr.lightcones.abundance.WarmthShortensColdNights;
import art.ameliah.hsr.lightcones.abundance.WhatIsReal;
import art.ameliah.hsr.lightcones.destruction.BrighterThanTheSun;
import art.ameliah.hsr.lightcones.destruction.DanceAtSunset;
import art.ameliah.hsr.lightcones.destruction.FlamesAfar;
import art.ameliah.hsr.lightcones.destruction.IShallBeMyOwnSword;
import art.ameliah.hsr.lightcones.destruction.IndeliblePromise;
import art.ameliah.hsr.lightcones.destruction.NowhereToRun;
import art.ameliah.hsr.lightcones.destruction.OnTheFallOfAnAeon;
import art.ameliah.hsr.lightcones.destruction.SomethingIrreplaceable;
import art.ameliah.hsr.lightcones.destruction.TheUnreachableSide;
import art.ameliah.hsr.lightcones.destruction.UnderTheBlueSky;
import art.ameliah.hsr.lightcones.destruction.WhereaboutsShouldDreamsRest;
import art.ameliah.hsr.lightcones.erudition.AfterTheCharmonyFall;
import art.ameliah.hsr.lightcones.erudition.AnInstantBeforeAGaze;
import art.ameliah.hsr.lightcones.erudition.BeforeDawn;
import art.ameliah.hsr.lightcones.erudition.EternalCalculus;
import art.ameliah.hsr.lightcones.erudition.GeniusesRepose;
import art.ameliah.hsr.lightcones.erudition.IntoTheUnreachableVeil;
import art.ameliah.hsr.lightcones.erudition.MakeTheWorldClamor;
import art.ameliah.hsr.lightcones.erudition.NightOnTheMilkyWay;
import art.ameliah.hsr.lightcones.erudition.TheBirthOfTheSelf;
import art.ameliah.hsr.lightcones.erudition.TheDayTheCosmosFell;
import art.ameliah.hsr.lightcones.erudition.TheSeriousnessOfBreakfast;
import art.ameliah.hsr.lightcones.erudition.TodayIsAnotherPeacefulDay;
import art.ameliah.hsr.lightcones.erudition.YetHopeIsPriceless;
import art.ameliah.hsr.lightcones.harmony.AGroundedAscent;
import art.ameliah.hsr.lightcones.harmony.ButTheBattleIsntOver;
import art.ameliah.hsr.lightcones.harmony.CarveTheMoonWeaveTheClouds;
import art.ameliah.hsr.lightcones.harmony.DanceDanceDance;
import art.ameliah.hsr.lightcones.harmony.DreamvilleAdventure;
import art.ameliah.hsr.lightcones.harmony.EarthlyEscapade;
import art.ameliah.hsr.lightcones.harmony.FlowingNightglow;
import art.ameliah.hsr.lightcones.harmony.ForTomorrowsJourney;
import art.ameliah.hsr.lightcones.harmony.IfTimeWereAFlower;
import art.ameliah.hsr.lightcones.harmony.MemoriesOfThePast;
import art.ameliah.hsr.lightcones.harmony.PastAndFuture;
import art.ameliah.hsr.lightcones.harmony.PastSelfInMirror;
import art.ameliah.hsr.lightcones.harmony.PlanetaryRendezvous;
import art.ameliah.hsr.lightcones.harmony.PoisedToBloom;
import art.ameliah.hsr.lightcones.hunt.BaptismOfPureThought;
import art.ameliah.hsr.lightcones.hunt.CruisingInTheStellarSea;
import art.ameliah.hsr.lightcones.hunt.FinalVictor;
import art.ameliah.hsr.lightcones.hunt.IVentureForthToHunt;
import art.ameliah.hsr.lightcones.hunt.InTheNight;
import art.ameliah.hsr.lightcones.hunt.OnlySilenceRemains;
import art.ameliah.hsr.lightcones.hunt.ReturnToDarkness;
import art.ameliah.hsr.lightcones.hunt.RiverFlowsInSpring;
import art.ameliah.hsr.lightcones.hunt.SailingTowardsASecondLife;
import art.ameliah.hsr.lightcones.hunt.ShadowedByNight;
import art.ameliah.hsr.lightcones.hunt.SleepLikeTheDead;
import art.ameliah.hsr.lightcones.hunt.SubscribeForMore;
import art.ameliah.hsr.lightcones.hunt.Swordplay;
import art.ameliah.hsr.lightcones.hunt.WorrisomeBlissful;
import art.ameliah.hsr.lightcones.nihility.AlongThePassingShore;
import art.ameliah.hsr.lightcones.nihility.BeforeTheTutorialMissionStarts;
import art.ameliah.hsr.lightcones.nihility.BoundlessChoreo;
import art.ameliah.hsr.lightcones.nihility.EyesOfThePrey;
import art.ameliah.hsr.lightcones.nihility.Fermata;
import art.ameliah.hsr.lightcones.nihility.GoodNightAndSleepWell;
import art.ameliah.hsr.lightcones.nihility.InTheNameOfTheWorld;
import art.ameliah.hsr.lightcones.nihility.IncessantRain;
import art.ameliah.hsr.lightcones.nihility.ItsShowtime;
import art.ameliah.hsr.lightcones.nihility.LiesAflutterInTheWind;
import art.ameliah.hsr.lightcones.nihility.PatienceIsAllYouNeed;
import art.ameliah.hsr.lightcones.nihility.ReforgedRemembrance;
import art.ameliah.hsr.lightcones.nihility.ResolutionShinesAsPearlsOfSweat;
import art.ameliah.hsr.lightcones.nihility.SolitaryHealing;
import art.ameliah.hsr.lightcones.nihility.ThoseManySprings;
import art.ameliah.hsr.lightcones.nihility.WeWillMeetAgain;
import art.ameliah.hsr.lightcones.preservation.ConcertForTwo;
import art.ameliah.hsr.lightcones.preservation.DayOneOfMyNewLife;
import art.ameliah.hsr.lightcones.preservation.DestinysThreadsForewoven;
import art.ameliah.hsr.lightcones.preservation.InherentlyUnjustDestiny;
import art.ameliah.hsr.lightcones.preservation.LandausChoice;
import art.ameliah.hsr.lightcones.preservation.MomentOfVictory;
import art.ameliah.hsr.lightcones.preservation.SheAlreadyShutHerEyes;
import art.ameliah.hsr.lightcones.preservation.TextureOfMemories;
import art.ameliah.hsr.lightcones.preservation.ThisIsMe;
import art.ameliah.hsr.lightcones.preservation.TrendOfTheUniversalMarket;
import art.ameliah.hsr.lightcones.preservation.WeAreWildfire;
import art.ameliah.hsr.lightcones.remembrance.MakeFarewellsMoreBeautiful;
import art.ameliah.hsr.lightcones.remembrance.MayRainbowsRemainInTheSky;
import art.ameliah.hsr.lightcones.remembrance.TimeWovenIntoGold;
import art.ameliah.hsr.lightcones.remembrance.VictoryInABlink;

public class LightconeRegistry extends AbstractRegistry<AbstractLightcone> {

    public static final LightconeRegistry INSTANCE = new LightconeRegistry();

    public LightconeRegistry() {
        register(21000, PostOpConversation.class);
        register(21001, GoodNightAndSleepWell.class);
        register(21002, DayOneOfMyNewLife.class);
        register(21003, OnlySilenceRemains.class);
        register(21004, MemoriesOfThePast.class);
        register(21006, TheBirthOfTheSelf.class);
        register(21007, SharedFeeling.class);
        register(21008, EyesOfThePrey.class);
        register(21009, LandausChoice.class);
        register(21010, Swordplay.class);
        register(21011, PlanetaryRendezvous.class);
        register(21013, MakeTheWorldClamor.class);
        register(21014, PerfectTiming.class);
        register(21015, ResolutionShinesAsPearlsOfSweat.class);
        register(21016, TrendOfTheUniversalMarket.class);
        register(21017, SubscribeForMore.class);
        register(21018, DanceDanceDance.class);
        register(21019, UnderTheBlueSky.class);
        register(21020, GeniusesRepose.class);
        register(21021, QuidProQuo.class);
        register(21022, Fermata.class);
        register(21023, WeAreWildfire.class);
        register(21024, RiverFlowsInSpring.class);
        register(21025, PastAndFuture.class);
        register(21027, TheSeriousnessOfBreakfast.class);
        register(21028, WarmthShortensColdNights.class);
        register(21029, WeWillMeetAgain.class);
        register(21030, ThisIsMe.class);
        register(21031, ReturnToDarkness.class);
        register(21032, CarveTheMoonWeaveTheClouds.class);
        register(21033, NowhereToRun.class);
        register(21034, TodayIsAnotherPeacefulDay.class);
        register(21035, WhatIsReal.class);
        register(21036, DreamvilleAdventure.class);
        register(21037, FinalVictor.class);
        register(21038, FlamesAfar.class);
        register(21039, DestinysThreadsForewoven.class);
        register(21040, TheDayTheCosmosFell.class);
        register(21041, ItsShowtime.class);
        register(21042, IndeliblePromise.class);
        register(21043, ConcertForTwo.class);
        register(21044, BoundlessChoreo.class);
        register(21045, AfterTheCharmonyFall.class);
        register(21046, PoisedToBloom.class);
        register(21047, ShadowedByNight.class);
        register(22000, BeforeTheTutorialMissionStarts.class);
        register(22001, HeyOverHere.class);
        register(22002, ForTomorrowsJourney.class);
        register(23000, NightOnTheMilkyWay.class);
        register(23001, InTheNight.class);
        register(23002, SomethingIrreplaceable.class);
        register(23003, ButTheBattleIsntOver.class);
        register(23004, InTheNameOfTheWorld.class);
        register(23005, MomentOfVictory.class);
        register(23006, PatienceIsAllYouNeed.class);
        register(23007, IncessantRain.class);
        register(23008, EchoesOftheCoffin.class);
        register(23009, TheUnreachableSide.class);
        register(23010, BeforeDawn.class);
        register(23011, SheAlreadyShutHerEyes.class);
        register(23012, SleepLikeTheDead.class);
        register(23013, TimeWaitsForNoOne.class);
        register(23014, IShallBeMyOwnSword.class);
        register(23015, BrighterThanTheSun.class);
        register(23016, WorrisomeBlissful.class);
        register(23017, NightOfFright.class);
        register(23018, AnInstantBeforeAGaze.class);
        register(23019, PastSelfInMirror.class);
        register(23020, BaptismOfPureThought.class);
        register(23021, EarthlyEscapade.class);
        register(23022, ReforgedRemembrance.class);
        register(23023, InherentlyUnjustDestiny.class);
        register(23024, AlongThePassingShore.class);
        register(23025, WhereaboutsShouldDreamsRest.class);
        register(23026, FlowingNightglow.class);
        register(23027, SailingTowardsASecondLife.class);
        register(23028, YetHopeIsPriceless.class);
        register(23029, ThoseManySprings.class);
        register(23030, DanceAtSunset.class);
        register(23031, IVentureForthToHunt.class);
        register(23032, ScentAloneStaysTrue.class);
        register(24000, OnTheFallOfAnAeon.class);
        register(24001, CruisingInTheStellarSea.class);
        register(24002, TextureOfMemories.class);
        register(24003, SolitaryHealing.class);
        register(24004, EternalCalculus.class);
        register(23034, AGroundedAscent.class);
        register(23036, TimeWovenIntoGold.class);
        register(23037, IntoTheUnreachableVeil.class);
        register(23038, IfTimeWereAFlower.class);
        register(21050, VictoryInABlink.class);
        register(23040, MakeFarewellsMoreBeautiful.class);
        register(23043, LiesAflutterInTheWind.class);
        register(23042, MayRainbowsRemainInTheSky.class);
    }

    public AbstractLightcone getLightCone(int id, AbstractCharacter<?> owner) throws Exception {
        if (registry.containsKey(id)) {
            return registry.get(id).getConstructor(AbstractCharacter.class).newInstance(owner);
        }
        throw new RuntimeException("Element with id: " + id + " not found");
    }

}
