package art.ameliah.hsr.lightcones.harmony;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.events.combat.TurnStartEvent;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.NoSuchElementException;

public class CarveTheMoonWeaveTheClouds extends AbstractLightcone {

    public CarveTheMoonWeaveTheClouds(AbstractCharacter<?> owner) {
        super(953, 476, 331, owner);
    }

    private void refreshEffects() {
        int type = getBattle().getWeaveEffectRng().nextInt(3) + 1;
        CarveTheMoonWeaveTheCloudsEffect effect = new CarveTheMoonWeaveTheCloudsEffect(type);
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.removePower(effect.getName());
            character.addPower(effect);
        }
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        refreshEffects();
    }

    @Subscribe
    public void onTurnStart(TurnStartEvent e) {
        refreshEffects();
    }

    public static class CarveTheMoonWeaveTheCloudsEffect extends PermPower {

        public CarveTheMoonWeaveTheCloudsEffect(int type) {
            super("Carve the Moon, Weave the Clouds effect");
            switch (type) {
                case 1:
                    this.setStat(PowerStat.ATK_PERCENT, 20);
                    break;
                case 2:
                    this.setStat(PowerStat.CRIT_DAMAGE, 24);
                    break;
                case 3:
                    this.setStat(PowerStat.ENERGY_REGEN, 12);
                    break;
                default:
                    throw new NoSuchElementException();
            }
        }
    }
}
