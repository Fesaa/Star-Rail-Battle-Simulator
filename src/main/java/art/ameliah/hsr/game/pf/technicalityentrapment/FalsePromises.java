package art.ameliah.hsr.game.pf.technicalityentrapment;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.wave.pf.PfBattle;
import art.ameliah.hsr.battleLogic.wave.pf.PureFictionBuff;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;

import java.util.List;

public class FalsePromises implements PureFictionBuff {
    @Override
    public void applyGritMechanic(PfBattle battle) {
        battle.getPlayers().forEach(player -> player.addPower(new PermPower() {
            @Override
            public void afterAttackFinish(Attack attack) {
                if (attack.getTypes().contains(DamageType.FOLLOW_UP)) {
                    battle.increaseGridAmount(2 * attack.getTargets().size());
                }
            }
        }));
    }

    @Override
    public void applySurgingGritBuff(PfBattle battle) {

    }

    @Override
    public void removeSurgingGritBuff(PfBattle battle) {

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
        public void beforeAttacked(Attack attack) {
            if (!attack.getTypes().contains(DamageType.FOLLOW_UP)) {
                return;
            }

            for (var target : attack.getTargets()) {
                int idx = getBattle().getEnemies().indexOf(target);

                // TODO: Figure out how much dmg
                //attack.hitFixed(target, 0);
                //getBattle().enemyCallback(idx-1, t -> attack.hitFixed(t, 0));
                //getBattle().enemyCallback(idx+1, t -> attack.hitFixed(t, 0));
            }
        }
    }
}
