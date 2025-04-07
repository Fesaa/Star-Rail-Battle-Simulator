package art.ameliah.hsr.lightcones.remembrance;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostSummon;
import art.ameliah.hsr.events.character.PostUseOnAllies;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class VictoryInABlink extends AbstractLightcone {

    public VictoryInABlink(AbstractCharacter<?> owner) {
        super(847, 476, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_DAMAGE, 24, "Victory In a Blink CD Boost"));
    }

    @Subscribe
    public void afterSummon(PostSummon e) {
        e.getMemosprite().addPower(new VictoryInABlinkListener());
    }

    public static class VictoryInABlinkListener extends PermPower {
        public VictoryInABlinkListener() {
            super("Victory In a Blink Listener");
        }

        @Subscribe
        public void afterUseOnAlly(PostUseOnAllies e) {
            // Any implies one? Not sure
            if (e.getAllies().size() != 1) {
                return;
            }

            getBattle().getPlayers().forEach(player -> {
                player.addPower(TempPower.create(PowerStat.DAMAGE_BONUS, 16, 3, "Victory In a Blink DMG Boost"));
            });
        }
    }
}
