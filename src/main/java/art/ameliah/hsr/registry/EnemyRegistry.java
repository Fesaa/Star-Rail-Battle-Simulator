package art.ameliah.hsr.registry;

import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.game.cosmos.LordyTrashcan;
import art.ameliah.hsr.enemies.game.jarilovi.AutomatonBeetle;
import art.ameliah.hsr.enemies.game.jarilovi.IncinerationShadewalker;
import art.ameliah.hsr.enemies.game.luofu.EntrancedIngeniumIlluminationDragonfish;
import art.ameliah.hsr.enemies.game.luofu.SableclawWolftrooper;
import art.ameliah.hsr.enemies.game.penacony.PastConfinedAndCaged;

import java.util.HashMap;
import java.util.Map;

public class EnemyRegistry {

    private static final Map<Integer, Class<? extends AbstractEnemy>> register = new HashMap<>();

    static {
        register(1012030, AutomatonBeetle.class);
        register(2012010, EntrancedIngeniumIlluminationDragonfish.class);
        register(3002050, LordyTrashcan.class);
        register(300205006, LordyTrashcan.class);
        register(2032010, SableclawWolftrooper.class);
        register(1022020, IncinerationShadewalker.class);
        register(3003030, PastConfinedAndCaged.class);
        register(300303006, PastConfinedAndCaged.class);
    }

    private static void register(int id, Class<? extends AbstractEnemy> clazz) {
        try {
            register.put(id, clazz);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public static AbstractEnemy getEnemy(int id) throws Exception {
        if (!register.containsKey(id)) {
            throw new RuntimeException("Packet with id " + id + " is not registered.");
        } else {
            return register.get(id).getConstructor().newInstance();
        }
    }

}
