package art.ameliah.hsr.registry;

import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.game.cosmos.LordyTrashcan;
import art.ameliah.hsr.enemies.game.jarilovi.AutomatonBeetle;
import art.ameliah.hsr.enemies.game.jarilovi.AutomatonHound;
import art.ameliah.hsr.enemies.game.jarilovi.Gepard;
import art.ameliah.hsr.enemies.game.jarilovi.fragmentum.IncinerationShadewalker;
import art.ameliah.hsr.enemies.game.jarilovi.SilvermaneCannoneer;
import art.ameliah.hsr.enemies.game.luofu.EntrancedIngeniumIlluminationDragonfish;
import art.ameliah.hsr.enemies.game.luofu.SableclawWolftrooper;
import art.ameliah.hsr.enemies.game.penacony.PastConfinedAndCaged;

import java.util.HashMap;
import java.util.Map;

public class EnemyRegistry extends AbstractRegistry<AbstractEnemy> {

    public static final EnemyRegistry INSTANCE = new EnemyRegistry();

    private EnemyRegistry() {
        register(1012030, AutomatonBeetle.class);
        register(2012010, EntrancedIngeniumIlluminationDragonfish.class);
        register(3002050, LordyTrashcan.class);
        register(300205006, LordyTrashcan.class);
        register(2032010, SableclawWolftrooper.class);
        register(1022020, IncinerationShadewalker.class);
        register(3003030, PastConfinedAndCaged.class);
        register(300303006, PastConfinedAndCaged.class);
        register(1002030, SilvermaneCannoneer.class);
        register(1012010, AutomatonHound.class);
        register(100402014, Gepard.class);
    }

}
