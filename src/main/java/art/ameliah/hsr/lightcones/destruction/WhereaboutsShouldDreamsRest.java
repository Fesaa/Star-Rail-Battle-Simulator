package art.ameliah.hsr.lightcones.destruction;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import java.util.ArrayList;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class WhereaboutsShouldDreamsRest extends AbstractLightcone {
    public WhereaboutsShouldDreamsRest(AbstractCharacter<?> owner) {
        super(1164, 476, 529, owner);
        throw new UnsupportedOperationException("Not implemented, speed debugs not working properly.");
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.BREAK_EFFECT, 60, "Whereabouts Should Dreams Rest Break Boost"));
    }

    @Override
    public void onAttack(AbstractCharacter<?> character, ArrayList<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
        for (AbstractEnemy enemy : enemiesHit) {
            enemy.addPower(new Routed(this));
        }
    }

    public static class Routed extends TempPower {

        private final WhereaboutsShouldDreamsRest lightcone;

        public Routed(WhereaboutsShouldDreamsRest lightcone) {
            this.name = this.getClass().getSimpleName();
            this.turnDuration = 2;
            this.type = PowerType.DEBUFF;
            this.lightcone = lightcone;
            this.setStat(PowerStat.SPEED_PERCENT, -20);
        }

        @Override
        public float getConditionalDamageTaken(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            if (!damageTypes.contains(DamageType.BREAK)) return 0;
            if (character != this.lightcone.owner) return 0;
            return 24;
        }
    }

}
