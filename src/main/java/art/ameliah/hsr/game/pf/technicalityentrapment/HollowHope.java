package art.ameliah.hsr.game.pf.technicalityentrapment;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.wave.pf.PfBattle;
import art.ameliah.hsr.battleLogic.wave.pf.PureFictionBuff;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;

/**
 * TODO: Finish, this one is break. Which isn't properly implemented
 */
public class HollowHope implements PureFictionBuff {
    @Override
    public void applyGritMechanic(PfBattle battle) {
        battle.getPlayers().forEach(player -> player.addPower(new PermPower("Hollow Hope Grit Mechanic") {

            private int weaknessBroken = 0;

            @Override
            public void beforeAttack(Attack attack) {
                this.weaknessBroken = attack.getTargets()
                        .stream()
                        .filter(AbstractEnemy::isWeaknessBroken)
                        .mapToInt(_ -> 1)
                        .sum();
            }

            @Override
            public void afterAttack(Attack attack) {
                int diff = attack.getTargets()
                        .stream()
                        .filter(AbstractEnemy::isWeaknessBroken)
                        .mapToInt(_ -> 1)
                        .sum() - this.weaknessBroken;
                this.weaknessBroken = 0;

                battle.increaseGridAmount(3 * Math.max(diff, 0));
            }
        }));
    }

    @Override
    public void applySurgingGritBuff(PfBattle battle) {
        // TODO: Break stuff
    }

    @Override
    public void removeSurgingGritBuff(PfBattle battle) {
        // TODO: Break stuff
    }
}
