package art.ameliah.hsr.game.pf.technicalityentrapment;

import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.battleLogic.wave.pf.ISurgingGrit;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
        battle.getPlayers().forEach(e -> e.addPower(new SurgingGritPlayerPower()));
    }

    @Override
    public void remove(IBattle battle) {
        battle.getEnemies().forEach(e -> e.removePower(SurgingGritPower.NAME));
        battle.getPlayers().forEach(e -> e.removePower(SurgingGritPlayerPower.NAME));
    }

    public static class SurgingGritPlayerPower extends PermPower {
        public static String NAME = "Surging Grit Player Power";
        @Override
        public void afterAttack(AttackLogic attack) {
            if (!attack.getTypes().contains(DamageType.SKILL)) {
                return;
            }

            List<AbstractEnemy> targetsCopy = new ArrayList<>(attack.getTargets());
            for (var target : targetsCopy) {
                int idx = getBattle().getEnemies().indexOf(target);

                // https://youtu.be/INVTD86xO_Q?si=lQVkPJl4rB5vt0hK&t=502 <- got dmg from
                attack.hitFixed(this, target, 11013);
                getBattle().enemyCallback(idx - 1, t -> attack.hitFixed(this, t, 11013));
                getBattle().enemyCallback(idx + 1, t -> attack.hitFixed(this, t, 11013));
            }
        }
    }

    public static class SurgingGritPower extends PermPower {
        public static String NAME = "Surging Grit Power";

        public SurgingGritPower() {
            super(NAME);
        }

        @Override
        public float getConditionalDamageTaken(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            return 50;
        }
    }
}
