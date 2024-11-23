package art.ameliah.hsr.battleLogic.combat;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.log.lines.character.Attacked;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.moze.Moze;
import art.ameliah.hsr.enemies.AbstractEnemy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EnemyAttack implements IAttack, BattleParticipant {

    private final AbstractEnemy source;
    private final List<EnemyHit> hits = new ArrayList<>();

    private boolean hasExecuted = false;

    public EnemyAttack(AbstractEnemy source) {
        this.source = source;
    }

    @Override
    public void execute(boolean forceFirst) {
        if (getBattle().isAttacking()) {
            if (forceFirst) {
                getBattle().attackQueue().offerFirst(this);
            } else {
                getBattle().attackQueue().offerLast(this);
            }

            return;
        }
        getBattle().setAttacking(true);

        for (EnemyHit hit : this.hits) {
            if (hit.target() instanceof Moze moze) {
                if (moze.isDeparted) {
                    continue;
                }
            }

            // TODO: Deal player dmg
            getBattle().addToLog(new Attacked(source, hit.target, hit.dmg, List.of()));
            hit.target.emit(l -> l.afterAttacked(hit.target, source, new ArrayList<>(), hit.energy, hit.dmg));
        }

        this.hasExecuted = true;
        getBattle().setAttacking(false);
    }

    public EnemyAttack hit(Collection<AbstractCharacter<?>> targets, int energy, float dmg) {
        for (AbstractCharacter<?> target : targets) {
            this.hit(target, energy, dmg);
        }
        return this;
    }

    public EnemyAttack hit(Collection<AbstractCharacter<?>> targets, float dmg) {
        return this.hit(targets, 0, dmg);
    }

    public EnemyAttack hit(AbstractCharacter<?> target, float dmg) {
        return this.hit(target, 0, dmg);
    }

    public EnemyAttack hit(AbstractCharacter<?> target, int energyToGain) {
        return this.hit(target, energyToGain, this.source.attackDmg());
    }

    public EnemyAttack hit(AbstractCharacter<?> target) {
        return this.hit(target, 0, this.source.attackDmg());
    }

    public EnemyAttack hit(AbstractCharacter<?> target, int energyToGain, float dmg) {
        if (this.hasExecuted) {
            throw new IllegalStateException("Cannot add hits after executing");
        }
        this.hits.add(new EnemyHit(target, energyToGain, dmg));
        return this;
    }

    @Override
    public IBattle getBattle() {
        return this.source.getBattle();
    }

    public record EnemyHit(AbstractCharacter<?> target, int energy, float dmg) {}
}
