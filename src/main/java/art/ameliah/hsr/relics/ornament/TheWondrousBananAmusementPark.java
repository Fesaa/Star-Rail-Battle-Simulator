package art.ameliah.hsr.relics.ornament;

import art.ameliah.hsr.battleLogic.AbstractSummon;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.AbstractSummoner;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
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

    @Override
    public void onCombatStart() {
        this.owner.addPower(new TheWondrousBananAmusementParkCD());
    }

    public static class TheWondrousBananAmusementParkCD extends PermPower {
        public TheWondrousBananAmusementParkCD() {
            super("The Wondrous Banan Amusement extra Park CD boost");
        }

        @Override
        public float getConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (!(character instanceof AbstractSummoner)) {
                return 0;
            }

            List<? extends AbstractSummon<?>> allSummons = ((AbstractSummoner<?>) character).getSummons();
            boolean activeSummon = getBattle().getActionValueMap()
                    .keySet()
                    .stream()
                    .filter(c -> c instanceof AbstractSummon)
                    .map(c -> (AbstractSummon<?>) c)
                    .anyMatch(allSummons::contains);

            if (activeSummon) {
                return 32;
            }

            return 0;
        }
    }
}
