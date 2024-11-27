package art.ameliah.hsr.game.pf.technicalityentrapment;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.battleLogic.wave.pf.PfBattle;
import art.ameliah.hsr.battleLogic.wave.pf.PureFictionBuff;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;

import java.util.List;

public class FalsePromises implements PureFictionBuff {

    private final AbstractPower power = new FalsePromisesPower();

    @Override
    public void applyGritMechanic(PfBattle battle) {
        battle.getPlayers().forEach(player -> player.addPower(new PermPower("False Promises grit mechanic") {
            @Override
            public void afterAttack(AttackLogic attack) {
                if (attack.getTypes().contains(DamageType.FOLLOW_UP)) {
                    battle.increaseGridAmount(2 * attack.getTargets().size());
                }
            }
        }));
    }

    @Override
    public void applySurgingGritBuff(PfBattle battle) {
        battle.getPlayers().forEach(player -> player.addPower(power));
    }

    @Override
    public void removeSurgingGritBuff(PfBattle battle) {
        battle.getPlayers().forEach(player -> player.removePower(FalsePromisesPower.NAME));
    }

    public static class FalsePromisesPower extends PermPower {
        public static final String NAME = "FalsePromisesPower";

        public FalsePromisesPower() {
            super(NAME);
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (damageTypes.contains(DamageType.FOLLOW_UP)) {
                return 40;
            }
            return 0;
        }

        @Override
        public void afterAttack(AttackLogic attack) {
            if (!attack.getTypes().contains(DamageType.FOLLOW_UP)) {
                return;
            }

            for (var target : attack.getTargets()) {
                int idx = getBattle().getEnemies().indexOf(target);

                // https://youtu.be/INVTD86xO_Q?si=s8wUOlX6jAEUlSpi&t=497 <- got dmg from
                attack.hitFixed(this, target, 5930);
                getBattle().enemyCallback(idx - 1, t -> attack.hitFixed(FalsePromisesPower.this, t, 5930));
                getBattle().enemyCallback(idx + 1, t -> attack.hitFixed(FalsePromisesPower.this, t, 5930));
            }
        }
    }
}
