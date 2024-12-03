package art.ameliah.hsr.battleLogic.combat.enemy;

import art.ameliah.hsr.battleLogic.combat.hit.EnemyHit;
import art.ameliah.hsr.battleLogic.combat.result.EnemyHitResult;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class EnemyAttackLogic {

    @Getter
    private final AbstractEnemy source;
    @Getter
    private final Collection<AbstractCharacter<?>> targets;
    @Getter
    private final Collection<DamageType> types;

    private final Function<EnemyHit, EnemyHitResult> callback;

    public Collection<EnemyHitResult> hit(Collection<AbstractCharacter<?>> targets, float dmg) {
        return this.hit(targets, 0, dmg);
    }

    public Collection<EnemyHitResult> hit(Collection<AbstractCharacter<?>> targets, int energy, float dmg) {
        List<EnemyHitResult> results = new ArrayList<>();
        for (AbstractCharacter<?> target : targets) {
            results.add(this.hit(target, energy, dmg));
        }
        return results;
    }

    public EnemyHitResult hit(AbstractCharacter<?> target, float dmg) {
        return this.hit(target, 0, dmg);
    }

    public EnemyHitResult hit(AbstractCharacter<?> target, int energy, float dmg) {
        if (!this.targets.contains(target)) {
            throw new IllegalStateException("Cannot hit target that isn't part of Attack");
        }

        return this.addHit(new EnemyHit(target, energy, dmg));
    }

    private EnemyHitResult addHit(EnemyHit hit) {
        return this.callback.apply(hit);
    }



}
