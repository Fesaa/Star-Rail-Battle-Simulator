package art.ameliah.hsr.battleLogic.combat.base;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.characters.DamageType;
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

@Getter
@RequiredArgsConstructor
public class AbstractDelayAttack<S extends BattleParticipant, T extends BattleParticipant, A> implements BattleParticipant {

    protected final S source;
    private final Set<DamageType> types = new HashSet<>();
    private final Set<T> targets = new HashSet<>();
    private final List<Runnable> hooks = new ArrayList<>();
    private Consumer<A> logic;

    public void afterAttackHook(Runnable hook) {
        this.hooks.add(hook);
    }

    public void addTypes(DamageType... types) {
        this.types.addAll(Arrays.asList(types));
    }

    public void addTypes(Collection<DamageType> types) {
        this.types.addAll(types);
    }

    public void addEnemies(Collection<T> enemies) {
        this.targets.addAll(enemies);
    }

    public void addEnemy(T enemy) {
        this.targets.add(enemy);
    }

    @SafeVarargs
    public final void addEnemies(T... enemies) {
        this.targets.addAll(Arrays.asList(enemies));
    }

    /**
     * Helper method to add a target at the same time, as setting logic.
     * To be used for logic that only uses that enemy
     *
     * @param target the target to add
     * @param logic  the logic to execute when attacking
     */
    public void logic(T target, Consumer<A> logic) {
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
    public void logic(Collection<T> targets, Consumer<A> logic) {
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
    public void logic(T target, BiConsumer<T, A> logic) {
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
    public void logic(Collection<T> targets, BiConsumer<Collection<T>, A> logic) {
        this.addEnemies(targets);
        this.logic(al -> logic.accept(targets, al));
    }

    /**
     * Sets or appends the logic to existing logic with {@link Consumer#andThen(Consumer)}
     *
     * @param logic logic to execute when attacking
     */
    public void logic(Consumer<A> logic) {
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
