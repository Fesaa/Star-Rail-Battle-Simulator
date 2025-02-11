package art.ameliah.hsr.game.moc.outofhome;

import art.ameliah.hsr.battleLogic.combat.enemy.EnemyAttackLogic;
import art.ameliah.hsr.battleLogic.combat.hit.FixedHit;
import art.ameliah.hsr.battleLogic.wave.moc.MocTurbulence;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.metrics.CounterMetric;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class Turbulence extends MocTurbulence {

    CounterMetric<Integer> hitCounter = metricRegistry.register(CounterMetric.newIntegerCounter("outofhome::turbulence::hitcounter"));

    public Turbulence(String name) {
        super(name);
    }

    @Override
    public void onCombatStart() {
        getBattle().registerForPlayers(p -> p.addPower(new TurbulenceBuff()));
        getBattle().registerForEnemy(e -> e.addPower(new TurbulenceBuff()));
    }

    @Override
    protected void trigger() {
        for (int i = 0; i < this.hitCounter.get(); i++) {
            AbstractEnemy enemy = getBattle().getRandomEnemy();
            /*
             * Source: https://www.youtube.com/watch?v=-j3xFugBVBk
             * Comment: The damage isn't fixed like this. It differs per enemy that's being hit.
             * But they all seem to have the same DEF. Enemy stats are rather hard to figure out :(
             * This will have to do.
             */
            enemy.hitDirectly(new FixedHit(null, this, enemy, 12500));
        }

        this.hitCounter.set(0);
    }

    public class TurbulenceBuff extends PermPower {
        public TurbulenceBuff() {
            super("Out of Home Turbulence Buff");

            this.setStat(PowerStat.HP_PERCENT, 30);
        }

        @Override
        public void afterHPLost(float amount) {
            if (!(this.getOwner() instanceof AbstractCharacter<?> character)) {
                return;
            }

            character.increaseHealth(this, character.getFinalHP()*0.1f, false);
            Turbulence.this.hitCounter.increase(2, 20);
        }

        @Override
        public void afterAttack(EnemyAttackLogic attack) {
            if (!(this.getOwner() instanceof AbstractEnemy)) {
                return;
            }


            attack.getTargets().forEach(c -> {
                c.increaseHealth(this, c.getFinalHP()*0.05f, false);
            });
            Turbulence.this.hitCounter.increase(1, 20);
        }
    }
}
