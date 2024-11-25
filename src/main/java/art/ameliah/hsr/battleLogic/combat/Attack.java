package art.ameliah.hsr.battleLogic.combat;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.combat.hit.Hit;
import art.ameliah.hsr.battleLogic.combat.result.HitResult;
import art.ameliah.hsr.battleLogic.log.lines.character.AttackEnd;
import art.ameliah.hsr.battleLogic.log.lines.character.AttackStart;
import art.ameliah.hsr.battleLogic.log.lines.character.Attacked;
import art.ameliah.hsr.battleLogic.log.lines.character.FailedHit;
import art.ameliah.hsr.battleLogic.log.lines.character.HitResultLine;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Attack implements IAttack, BattleParticipant {

    @Getter
    private final AbstractCharacter<?> source;
    @Getter
    private final Set<DamageType> types = new HashSet<>();
    @Getter
    private final Set<AbstractEnemy> targets = new HashSet<>();

    private final List<Runnable> afterAttackHooks = new ArrayList<>();

    private Consumer<DelayAttack> consumers = null;
    private float dmgDealt = 0;
    private boolean hasCompleted = false;

    public Attack(AbstractCharacter<?> source) {
        this.source = source;
    }

    /**
     * Ease of use method, as types are commonly known
     *
     * @param type     the type of the attack to happen
     * @param consumer attack logic
     * @return this
     */
    public Attack handle(DamageType type, Consumer<DelayAttack> consumer) {
        this.types.add(type);
        this.handle(consumer);
        return this;
    }

    public Attack handle(Consumer<DelayAttack> consumer) {
        if (this.consumers != null) {
            throw new IllegalStateException("This attack already has a handle");
        }

        this.consumers = consumer;
        return this;
    }


    public Attack afterAttackHook(Runnable hook) {
        if (this.hasCompleted) {
            throw new IllegalStateException("Attack has already completed");
        }

        this.afterAttackHooks.add(hook);
        return this;
    }

    @Override
    public void execute(boolean forceFirst) {
        if (getBattle().isAttacking()) {
            getBattle().addToQueue(this, forceFirst);
            return;
        }
        getBattle().setAttacking(true);

        DelayAttack dh = new DelayAttack(this.source);
        this.consumers.accept(dh); // Load attack logic

        if (dh.logic == null) {
            throw new IllegalStateException("This attack has no logic to execute");
        }

        this.types.addAll(dh.types);
        this.targets.addAll(dh.targets);
        this.afterAttackHooks.addAll(dh.hooks);

        getBattle().addToLog(new AttackStart(this));

        AttackLogic attackLogic = new AttackLogic(this.source, this.targets, this.types, this::handleHit);

        this.source.emit(l -> l.beforeAttack(attackLogic));
        this.targets.forEach(t -> t.emit(l -> l.beforeAttacked(attackLogic)));

        dh.logic.accept(attackLogic);

        this.source.emit(l -> l.afterAttack(attackLogic));
        this.targets.forEach(t -> t.emit(l -> l.afterAttacked(attackLogic)));
        this.afterAttackHooks.forEach(Runnable::run);

        getBattle().addToLog(new Attacked(
                this.source,
                // Why is this needed? How come List<A extends B> instead good for List<B>?
                this.getTargets().stream().map(e -> (AbstractEntity) e).collect(Collectors.toSet()),
                this.dmgDealt,
                this.types));

        this.hasCompleted = true;
        getBattle().addToLog(new AttackEnd(this));
        getBattle().setAttacking(false);
    }

    private HitResult handleHit(Hit hit) {
        BattleParticipant source = hit.getSource();
        if (source instanceof AbstractCharacter<?> e) {
            e.emit(l -> l.beforeDoHit(hit));
        }
        hit.getTarget().emit(l -> l.beforeReceiveHit(hit));

        HitResult res = hit.getTarget().hit(hit);
        // TODO: Metrics update, record overflow etc
        if (res.success()) {
            this.dmgDealt += hit.finalDmg();

            getBattle().increaseTotalPlayerDmg(hit.finalDmg());
            getBattle().updateContribution(hit.getSource(), hit.finalDmg());
            getBattle().addToLog(new HitResultLine(res));
        } else {
            getBattle().addToLog(new FailedHit(hit));
        }
        return res;
    }


    @Override
    public IBattle getBattle() {
        return this.source.getBattle();
    }


    @RequiredArgsConstructor
    public static class DelayAttack implements BattleParticipant {

        private final AbstractCharacter<?> source;

        private final Set<DamageType> types = new HashSet<>();
        private final Set<AbstractEnemy> targets = new HashSet<>();
        private final List<Runnable> hooks = new ArrayList<>();
        private Consumer<AttackLogic> logic;

        public void afterAttackHook(Runnable hook) {
            this.hooks.add(hook);
        }

        public void addTypes(DamageType... types) {
            this.types.addAll(Arrays.asList(types));
        }

        public void addTypes(Collection<DamageType> types) {
            this.types.addAll(types);
        }

        /**
         * Add enemies at indexes
         *
         * @param indexes the indexes
         */
        public void addEnemies(int... indexes) {
            for (int idx : indexes) {
                getBattle().enemyCallback(idx, this::addEnemy);
            }
        }

        public void addEnemies(Collection<AbstractEnemy> enemies) {
            this.targets.addAll(enemies);
        }

        public void addEnemy(AbstractEnemy enemy) {
            this.targets.add(enemy);
        }

        public void addEnemies(AbstractEnemy... enemies) {
            this.targets.addAll(Arrays.asList(enemies));
        }

        /**
         * Add logic for an enemy at position idx. Uses {@link IBattle#enemyCallback(int, Consumer)}
         * This will also add the enemy to the target set
         *
         * @param idx   the enemies index
         * @param logic the logic to execute when attacking
         */
        public void logic(int idx, BiConsumer<AbstractEnemy, AttackLogic> logic) {
            getBattle().enemyCallback(idx, e -> {
                this.addEnemy(e);
                this.logic(al -> logic.accept(e, al));
            });
        }

        /**
         * Helper method to add a target at the same time, as setting logic.
         * To be used for logic that only uses that enemy
         *
         * @param target the target to add
         * @param logic  the logic to execute when attacking
         */
        public void logic(AbstractEnemy target, Consumer<AttackLogic> logic) {
            this.addEnemies(target);
            this.logic(logic);
        }

        /**
         * Helper method to add a target at the same time, as setting logic.
         * To be used for logic that only uses that enemy
         *
         * @param targets the targets to add
         * @param logic   the logic to execute when attacking
         */
        public void logic(Collection<AbstractEnemy> targets, Consumer<AttackLogic> logic) {
            this.addEnemies(targets);
            this.logic(logic);
        }

        /**
         * Helper method to add a target at the same time, as setting logic.
         * To be used for logic that only uses that enemy. Returning the enemy in the callback for ease of use
         *
         * @param target the target to add
         * @param logic  the logic to execute when attacking
         */
        public void logic(AbstractEnemy target, BiConsumer<AbstractEnemy, AttackLogic> logic) {
            this.addEnemies(target);
            this.logic(al -> logic.accept(target, al));
        }

        /**
         * Helper method to add a target at the same time, as setting logic.
         * To be used for logic that only uses that enemy. Returning the targets in the callback for ease of use
         *
         * @param targets the targets to add
         * @param logic   the logic to execute when attacking
         */
        public void logic(Collection<AbstractEnemy> targets, BiConsumer<Collection<AbstractEnemy>, AttackLogic> logic) {
            this.addEnemies(targets);
            this.logic(al -> logic.accept(targets, al));
        }

        /**
         * Sets or appends the logic to existing logic with {@link Consumer#andThen(Consumer)}
         *
         * @param logic logic to execute when attacking
         */
        public void logic(Consumer<AttackLogic> logic) {
            if (this.logic == null) {
                this.logic = logic;
                return;
            }

            this.logic = this.logic.andThen(logic);
        }

        @Override
        public IBattle getBattle() {
            return this.source.getBattle();
        }
    }
}
