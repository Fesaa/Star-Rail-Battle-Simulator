package art.ameliah.hsr.lightcones.abundance;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;

import java.util.List;

public class QuidProQuo extends AbstractLightcone {

    public QuidProQuo(AbstractCharacter<?> owner) {
        super(953, 423, 397, owner);
    }

    @Override
    public void onTurnStart() {
        List<AbstractCharacter<?>> characters = getBattle().getPlayers()
                .stream()
                .filter(c -> c.getCurrentEnergy().get() < c.maxEnergy / 2)
                .toList();

        if (characters.isEmpty()) return;

        AbstractCharacter<?> target = characters.get(getBattle().getQpqRng().nextInt(characters.size()));
        target.increaseEnergy(16, AbstractCharacter.LIGHTCONE_ENERGY_GAIN);
    }
}
