package art.ameliah.hsr.battleLogic.combat.base;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.combat.IAttack;
import art.ameliah.hsr.battleLogic.log.lines.character.AttackEnd;
import art.ameliah.hsr.battleLogic.log.lines.character.AttackStart;
import art.ameliah.hsr.battleLogic.log.lines.character.Attacked;
import art.ameliah.hsr.characters.DamageType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *
 * @param <S> Source type
 * @param <T> Target type
 * @param <A> AttackLogic type
 * @param <D> DelayAttack type
 */
public abstract class AbstractAttack<S extends AbstractEntity, T extends BattleParticipant, A, D extends AbstractDelayAttack<S, T, A>> implements IAttack, BattleParticipant {

    @Getter
    protected final S source;

    @Getter
    protected final Set<DamageType> types = new HashSet<>();

    @Getter
    protected final Set<T> targets = new LinkedHashSet<>();

    protected final List<Runnable> afterAttackHooks = new ArrayList<>();
    protected Consumer<D> consumer = null;

    @Getter
    protected float dmgDealt = 0;
    protected boolean hasCompleted = false;

    public AbstractAttack(S source) {
        this.source = source;
    }

    /**
     * Ease of use method, as types are commonly known
     *
     * @param type     the type of the attack to happen
     * @param consumer attack logic
     * @return this
     */
    public AbstractAttack<S, T, A, D> handle(DamageType type, Consumer<D> consumer) {
        this.types.add(type);
        this.handle(consumer);
        return this;
    }

    public AbstractAttack<S, T, A, D> handle(Consumer<D> consumer) {
        if (this.consumer != null) {
            throw new IllegalStateException("This attack already has a handle");
        }

        this.consumer = consumer;
        return this;
    }

    public AbstractAttack<S, T, A, D> afterAttackHook(Runnable hook) {
        if (this.hasCompleted) {
            throw new IllegalStateException("Attack has already completed");
        }

        this.afterAttackHooks.add(hook);
        return this;
    }

    @Override
    public final void execute(boolean forceFirst) {
        if (getBattle().isAttacking()) {
            getBattle().addToQueue(this, forceFirst);
            return;
        }
        getBattle().setAttacking(true);

        D dh = this.newDelayAttack(this.source);
        this.consumer.accept(dh); // Load attack logic

        if (dh.getLogic() == null) {
            throw new IllegalStateException("This attack has no logic to execute");
        }

        this.types.addAll(dh.getTypes());
        this.targets.addAll(dh.getTargets());
        this.afterAttackHooks.addAll(dh.getHooks());

        getBattle().addToLog(new AttackStart(this));

        this.attack(dh);

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

    protected abstract D newDelayAttack(S source);

    /**
     * This should include the logic right after AttackStart is logged, until before afterHook are called
     * @param dh the DelayAttack from {@link AbstractAttack#newDelayAttack(AbstractEntity)}
     */
    protected abstract void attack(D dh);

    @Override
    public IBattle getBattle() {
        return this.source.getBattle();
    }

}
