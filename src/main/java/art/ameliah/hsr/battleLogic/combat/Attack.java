package art.ameliah.hsr.battleLogic.combat;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.combat.hit.AllyHit;
import art.ameliah.hsr.battleLogic.combat.hit.DelayedHit;
import art.ameliah.hsr.battleLogic.combat.hit.FixedHit;
import art.ameliah.hsr.battleLogic.combat.hit.HitHolder;
import art.ameliah.hsr.battleLogic.log.lines.StringLine;
import art.ameliah.hsr.battleLogic.log.lines.character.AttackEnd;
import art.ameliah.hsr.battleLogic.log.lines.character.AttackStart;
import art.ameliah.hsr.battleLogic.log.lines.character.Attacked;
import art.ameliah.hsr.battleLogic.log.lines.character.FailedHit;
import art.ameliah.hsr.battleLogic.log.lines.character.HitResult;
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

public class Attack implements BattleParticipant, IAttack {

    @Getter
    private final AbstractCharacter<?> source;
    @Getter
    private final Set<DamageType> types;
    @Getter
    private final Set<AbstractEnemy> targets;
    private final List<HitHolder> hits = new ArrayList<>();
    private final Set<Runnable> afterAttacks = new HashSet<>(); // I think this isn't needed anymore

    private boolean hasExecuted = false;

    public Attack(AbstractCharacter<?> source, DamageType... types) {
        this.source = source;
        this.types = new HashSet<>(List.of(types));
        this.targets = new HashSet<>();
    }

    public void execute(boolean forceFirst) {
        if (getBattle().isAttacking()) {
            getBattle().addToQueue(this, forceFirst);
            return;
        }
        getBattle().setAttacking(true);

        // SCUFFED
        this.hits.forEach(h -> h.getHits().forEach(hit -> {
            this.targets.add(hit.getTarget());
            this.types.addAll(hit.getTypes());
        }));

        getBattle().addToLog(new AttackStart(this));

        this.source.emit(l -> l.beforeAttack(this));
        this.targets.forEach(t -> t.emit(l -> l.beforeAttacked(this)));

        // TODO: Move dmg logic until this loop
        // In AbstractEnemy, handle dying after an attack?
        Map<AbstractEnemy, Float> dmgMap = new HashMap<>();
        for (HitHolder hitHolder : this.hits) {
            hitHolder.getHits().forEach(hit -> {

                BattleParticipant source = hit.getSource();
                if (source instanceof AbstractCharacter<?> e) {
                    e.emit(l -> l.beforeDoHit(hit));
                }
                hit.getTarget().emit(l -> l.beforeReceiveHit(hit));


                var res = hit.getTarget().hit(hit);
                // TODO: Metrics update, record overflow etc
                if (res.success()) {
                    dmgMap.put(hit.getTarget(), dmgMap.getOrDefault(hit.getTarget(), 0.0f) + hit.finalDmg());

                    getBattle().increaseTotalPlayerDmg(hit.finalDmg());
                    getBattle().updateContribution(hit.getSource(), hit.finalDmg());
                    getBattle().addToLog(new HitResult(hit));
                } else {
                    dmgMap.putIfAbsent(hit.getTarget(), 0.0f);
                    getBattle().addToLog(new FailedHit(hit));
                }
            });
        }

        for (AbstractEnemy target : this.targets) {
            float dmg = dmgMap.get(target);

            target.emit(l -> l.afterAttacked(this));
            getBattle().addToLog(new Attacked(this.source, target, dmg, this.types.stream().toList()));
        }

        this.source.emit(l -> l.afterAttack(this));
        this.hasExecuted = true;

        this.afterAttacks.forEach(Runnable::run);

        getBattle().addToLog(new AttackEnd(this));
        getBattle().setAttacking(false);
    }

    /**
     * Hit an enemy target for a fixed amount of dmg, without a source
     *
     * @param target
     * @param dmg
     * @return
     */
    public Attack hitFixed(BattleParticipant source, AbstractEnemy target, float dmg) {
        if (this.hasExecuted) {
            throw new IllegalStateException("The attack has already finished");
        }
        this.targets.add(target);
        this.hits.add(new FixedHit(source, target, dmg));
        return this;
    }

    public Attack delay(Consumer<DelayedHit> consumer) {
        if (this.hasExecuted) {
            throw new IllegalStateException("The attack has already finished");
        }
        this.hits.add(new DelayedHit(this.source, null, this.types, consumer));
        return this;
    }

    /**
     * Attacks are not executed when hitting. If the logic of hitting the enemy depends on its state
     * You'll want to use this function, and use the callback to access the logic
     *
     * @param target The target
     * @param logic  Lambda function,
     * @return the attack being constructed
     */
    public Attack hitEnemy(AbstractEnemy target, Consumer<DelayedHit> logic) {
        if (this.hasExecuted) {
            throw new IllegalStateException("The attack has already finished");
        }
        this.targets.add(target);
        this.hits.add(new DelayedHit(this.source, target, this.types, logic));
        return this;
    }

    public Attack hitEnemy(AbstractEnemy target, float multiplier, MultiplierStat stat, float toughnessDamage, List<DamageType> types) {
        return this.hitEnemy(this.source, target, multiplier, stat, toughnessDamage, false, types);
    }

    public Attack hitEnemy(AbstractEnemy target, float multiplier, MultiplierStat stat, float toughnessDamage, DamageType... types) {
        return this.hitEnemy(this.source, target, multiplier, stat, toughnessDamage, false, List.of(types));
    }

    public Attack hitEnemy(AbstractEnemy target, float multiplier, MultiplierStat stat, float toughnessDamage) {
        return this.hitEnemy(this.source, target, multiplier, stat, toughnessDamage, false, this.types.stream().toList());
    }

    public Attack hitEnemyIgnoreWeakness(AbstractEnemy target, float multiplier, MultiplierStat stat, float toughnessDamage, List<DamageType> types) {
        return this.hitEnemy(this.source, target, multiplier, stat, toughnessDamage, true, types);
    }

    public Attack hitEnemyIgnoreWeakness(AbstractEnemy target, float multiplier, MultiplierStat stat, float toughnessDamage, DamageType... types) {
        return this.hitEnemy(this.source, target, multiplier, stat, toughnessDamage, true, List.of(types));
    }

    public Attack hitEnemyIgnoreWeakness(AbstractEnemy target, float multiplier, MultiplierStat stat, float toughnessDamage) {
        return this.hitEnemy(this.source, target, multiplier, stat, toughnessDamage, true, this.types.stream().toList());
    }

    public Attack hitEnemy(AbstractCharacter<?> source, AbstractEnemy target, float multiplier, MultiplierStat stat) {
        AllyHit hit = new AllyHit(source, target, multiplier, stat, List.of(), 0, source.elementType, false);
        return this.hitEnemy(hit);
    }

    public Attack hitEnemy(AbstractCharacter<?> source, AbstractEnemy target, float multiplier, MultiplierStat stat, float toughnessDamage, boolean ignoreWeakness, List<DamageType> types) {
        AllyHit hit = new AllyHit(source, target, multiplier, stat, types, toughnessDamage, source.elementType, ignoreWeakness);
        return this.hitEnemy(hit);
    }

    public Attack hitEnemy(AllyHit hit) {
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

    public Attack addAfterAttack(Runnable runnable) {
        if (this.hasExecuted) {
            throw new IllegalStateException("Attack has already run");
        }
        this.afterAttacks.add(runnable);
        return this;
    }

    @Override
    public IBattle getBattle() {
        return this.source.getBattle();
    }
}
