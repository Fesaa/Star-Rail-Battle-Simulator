package art.ameliah.hsr.battleLogic.combat;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.log.lines.character.Attacked;
import art.ameliah.hsr.battleLogic.log.lines.character.CritHitResult;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Attack implements BattleParticipant {

    @Getter
    private final AbstractCharacter<?> source;
    @Getter
    private final Set<DamageType> types;
    @Getter
    private final Set<AbstractEnemy> targets;
    private final List<IHit> hits = new ArrayList<>();
    private final Set<Runnable> afterAttacks = new HashSet<>();

    private boolean hasExecuted = false;

    public Attack(AbstractCharacter<?> source, DamageType ...types) {
        this.source = source;
        this.types = new HashSet<>(List.of(types));
        this.targets = new HashSet<>();
    }

    public void execute() {
        if (getBattle().isAttacking()) {
            getBattle().attackQueue().offer(this);
            return;
        }
        getBattle().setAttacking(true);


        this.source.emit(l -> l.onAttack(this));
        this.targets.forEach(t -> t.emit(l -> l.beforeAttacked(this)));

        // TODO: Move dmg logic until this loop
        // In AbstractEnemy, handle dying after an attack?
        Map<AbstractEnemy, Float> dmgMap = new HashMap<>();
        for (IHit hitHolder : this.hits) {
            hitHolder.getHits().forEach(hit -> {
                hit.getSource().emit(l -> l.onBeforeHitEnemy(hit));
                hit.getTarget().emit(l -> l.onBeforeHitEnemy(hit));

                float dmg = hit.finalDmg();
                float toughnessReduce = hit.finalToughnessReduction();

                getBattle().addToLog(new CritHitResult(hit.getSource(), hit.getTarget(), dmg));
                if (toughnessReduce > 0) {
                    hit.getTarget().reduceToughness(toughnessReduce);
                }

                dmgMap.put(hit.getTarget(), dmgMap.getOrDefault(hit.getTarget(), 0.0f) + dmg);

                getBattle().increaseTotalPlayerDmg(dmg);
                getBattle().updateContribution(hit.getSource(), dmg);
            });
        }

        for (AbstractEnemy target : this.targets) {
            float dmg = dmgMap.get(target);

            target.emit(l -> l.onAttacked(this.source, target, this.types.stream().toList(), 0, dmg));
            getBattle().addToLog(new Attacked(this.source, target, dmg, this.types.stream().toList()));
        }

        this.source.emit(l -> l.afterAttackFinish(this.source, this.targets, this.types.stream().toList()));
        this.hasExecuted = true;

        this.afterAttacks.forEach(Runnable::run);

        getBattle().setAttacking(false);
        if (!getBattle().attackQueue().isEmpty()) {
            Attack nextAttack = getBattle().attackQueue().poll();
            if (nextAttack == null) {
                throw new IllegalStateException("A null attack was added to queue");
            }
            nextAttack.execute();
        }
    }

    /**
     * Attacks are not executed when hitting. If the logic of hitting the enemy depends on its state
     * You'll want to use this function, and use the callback to access the logic
     * @param target The target
     * @param logic Lambda function,
     * @return the attack being constructed
     */
    public Attack hitEnemy(AbstractEnemy target, Consumer<DelayedHit> logic) {
        this.targets.add(target);
        this.hits.add(new DelayedHit(this.source, target, this.types, logic));
        return this;
    }

    public Attack hitEnemy(AbstractEnemy target, float multiplier, MultiplierStat stat, float toughnessDamage, List<DamageType> types) {
        return this.hitEnemy(this.source, target, multiplier, stat, toughnessDamage, false, types);
    }

    public Attack hitEnemy(AbstractEnemy target, float multiplier, MultiplierStat stat, float toughnessDamage, DamageType ...types) {
        return this.hitEnemy(this.source, target, multiplier, stat, toughnessDamage, false, List.of(types));
    }

    public Attack hitEnemy(AbstractEnemy target, float multiplier, MultiplierStat stat, float toughnessDamage) {
        return this.hitEnemy(this.source, target, multiplier, stat, toughnessDamage, false, this.types.stream().toList());
    }

    public Attack hitEnemyIgnoreWeakness(AbstractEnemy target, float multiplier, MultiplierStat stat, float toughnessDamage, List<DamageType> types) {
        return this.hitEnemy(this.source, target, multiplier, stat, toughnessDamage, true, types);
    }

    public Attack hitEnemyIgnoreWeakness(AbstractEnemy target, float multiplier, MultiplierStat stat, float toughnessDamage, DamageType ...types) {
        return this.hitEnemy(this.source, target, multiplier, stat, toughnessDamage, true, List.of(types));
    }

    public Attack hitEnemyIgnoreWeakness(AbstractEnemy target, float multiplier, MultiplierStat stat, float toughnessDamage) {
        return this.hitEnemy(this.source, target, multiplier, stat, toughnessDamage, true, this.types.stream().toList());
    }

    public Attack hitEnemy(AbstractCharacter<?> source, AbstractEnemy target, float multiplier, MultiplierStat stat) {
        Hit hit = new Hit(source, target, multiplier, stat, types.stream().toList(), 0, source.elementType, false);
        return this.hitEnemy(hit);
    }

    public Attack hitEnemy(AbstractCharacter<?> source, AbstractEnemy target, float multiplier, MultiplierStat stat, float toughnessDamage, boolean ignoreWeakness, List<DamageType> types) {
        Hit hit = new Hit(source, target, multiplier, stat, types, toughnessDamage, source.elementType, ignoreWeakness);
        return this.hitEnemy(hit);
    }

    public Attack hitEnemy(Hit hit) {
        if (this.hasExecuted) {
            throw new IllegalStateException("The attack has already finished");
        }
        this.hits.add(hit);
        this.targets.add(hit.getTarget());
        this.types.addAll(hit.getTypes());
        return this;
    }

    public Attack hitEnemies(Collection<AbstractEnemy> targets, float multiplier, MultiplierStat stat, float toughnessDamage, List<DamageType> types) {
        for (AbstractEnemy target : targets) {
            this.hitEnemy(target, multiplier, stat, toughnessDamage, types);
        }
        return this;
    }

    public Attack hitEnemies(Collection<AbstractEnemy> targets, float multiplier, MultiplierStat stat, float toughnessDamage, DamageType... types) {
        for (AbstractEnemy target : targets) {
            this.hitEnemy(target, multiplier, stat, toughnessDamage, types);
        }
        return this;
    }

    public Attack hitEnemies(Collection<AbstractEnemy> targets, float multiplier, MultiplierStat stat, float toughnessDamage) {
        for (AbstractEnemy target : targets) {
            this.hitEnemy(target, multiplier, stat, toughnessDamage);
        }
        return this;
    }

    public Attack hitEnemiesIgnoreWeakness(Collection<AbstractEnemy> targets, float multiplier, MultiplierStat stat, float toughnessDamage, List<DamageType> types) {
        for (AbstractEnemy target : targets) {
            this.hitEnemyIgnoreWeakness(target, multiplier, stat, toughnessDamage, types);
        }
        return this;
    }

    public Attack hitEnemiesIgnoreWeakness(Collection<AbstractEnemy> targets, float multiplier, MultiplierStat stat, float toughnessDamage, DamageType... types) {
        for (AbstractEnemy target : targets) {
            this.hitEnemyIgnoreWeakness(target, multiplier, stat, toughnessDamage, types);
        }
        return this;
    }

    public Attack hitEnemiesIgnoreWeakness(Collection<AbstractEnemy> targets, float multiplier, MultiplierStat stat, float toughnessDamage) {
        for (AbstractEnemy target : targets) {
            this.hitEnemyIgnoreWeakness(target, multiplier, stat, toughnessDamage);
        }
        return this;
    }

    public Attack hitEnemies(AbstractCharacter<?> source, Collection<AbstractEnemy> targets, float multiplier, MultiplierStat stat) {
        for (AbstractEnemy target : targets) {
            this.hitEnemy(source, target, multiplier, stat);
        }
        return this;
    }

    public void addAfterAttack(Runnable runnable) {
        if (this.hasExecuted) {
            throw new IllegalStateException("Attack has already run");
        }
        this.afterAttacks.add(runnable);
    }

    @Override
    public IBattle getBattle() {
        return this.source.getBattle();
    }
}
