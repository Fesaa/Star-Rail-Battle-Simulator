package art.ameliah.hsr.game.pf.technicalityentrapment;

import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.combat.AttackLogic;
import art.ameliah.hsr.battleLogic.wave.pf.ISurgingGrit;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SurgingGrit implements ISurgingGrit {

    private final SurgingGritPower power = new SurgingGritPower();

    @Override
    public @Nullable PermPower getEnemyPower() {
        return this.power;
    }

    @Override
    public void apply(IBattle battle) {
        battle.getPlayers().forEach(p -> battle.AdvanceEntity(p, 100));
    }

    @Override
    public void remove(IBattle battle) {
        battle.getEnemies().forEach(e -> e.removePower(SurgingGritPower.NAME));
    }

    public static class SurgingGritPower extends PermPower {
        public static String NAME = "Surging Grit Power";

        public SurgingGritPower() {
            super(NAME);
        }

        @Override
        public void beforeAttacked(AttackLogic attack) {
            if (!attack.getTypes().contains(DamageType.SKILL)) {
                return;
            }

            for (var target : attack.getTargets()) {
                int idx = getBattle().getEnemies().indexOf(target);

                // https://youtu.be/INVTD86xO_Q?si=lQVkPJl4rB5vt0hK&t=502 <- got dmg from
                attack.hitFixed(this, target, 11013);
                getBattle().enemyCallback(idx - 1, t -> attack.hitFixed(SurgingGritPower.this, t, 11013));
                getBattle().enemyCallback(idx + 1, t -> attack.hitFixed(SurgingGritPower.this, t, 11013));
            }
        }

        @Override
        public float getConditionalDamageTaken(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            return 50;
        }
    }
}
