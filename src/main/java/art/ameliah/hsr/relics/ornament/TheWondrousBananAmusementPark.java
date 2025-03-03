package art.ameliah.hsr.relics.ornament;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.Summoner;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.List;

public class TheWondrousBananAmusementPark extends AbstractRelicSetBonus {
    public TheWondrousBananAmusementPark(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public TheWondrousBananAmusementPark(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_DAMAGE, 16, "The Wondrous Banan Amusement Park CD boost"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        this.owner.addPower(new TheWondrousBananAmusementParkCD());
    }

    public static class TheWondrousBananAmusementParkCD extends PermPower {
        public TheWondrousBananAmusementParkCD() {
            super("The Wondrous Banan Amusement extra Park CD boost");
        }

        @Override
        public float getConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (!(character instanceof Summoner summoner)) {
                return 0;
            }

            if (summoner.getSummon() != null) {
                return 32;
            }

            return 0;
        }
    }
}
