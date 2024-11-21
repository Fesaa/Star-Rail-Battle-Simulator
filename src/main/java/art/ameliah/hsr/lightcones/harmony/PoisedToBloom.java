package art.ameliah.hsr.lightcones.harmony;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.List;
import java.util.stream.Collectors;

public class PoisedToBloom extends AbstractLightcone {

    public PoisedToBloom(AbstractCharacter<?> owner) {
        super(953, 423, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.ATK_PERCENT, 32, "Poised To Bloom ATK Boost"));
    }

    @Override
    public void onCombatStart() {
        getBattle().getPlayers()
                .stream()
                .filter(c -> !c.getPath().equals(Path.UNKNOWN))
                .collect(Collectors.groupingBy(AbstractCharacter::getPath))
                .values()
                .stream()
                .filter(abstractCharacters -> abstractCharacters.size() > 1)
                .flatMap(List::stream)
                .forEach(c -> c.addPower(PermPower.create(PowerStat.CRIT_DAMAGE, 32, "Poised To Bloom Crit Damage Boost")));
    }
}
