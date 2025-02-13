package art.ameliah.hsr.registry;

import art.ameliah.hsr.battleLogic.wave.moc.Moc;

public class MocRegistry extends AbstractRegistry<Moc> {

    public static MocRegistry INSTANCE = new MocRegistry();

    private MocRegistry() {

    }
}
