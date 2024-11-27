package art.ameliah.hsr.battleLogic.combat.ally;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.battleLogic.combat.hit.AllyHit;
import art.ameliah.hsr.battleLogic.combat.hit.FixedHit;
import art.ameliah.hsr.battleLogic.combat.hit.Hit;
import art.ameliah.hsr.battleLogic.combat.result.HitResult;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

// TODO: Fix types for some additional hits
@RequiredArgsConstructor
public class AttackLogic {

    @Getter
    private final AbstractCharacter<?> source;
    @Getter
    private final Collection<AbstractEnemy> targets;
    @Getter
    private final Collection<DamageType> types;
    private final Function<Hit, HitResult> callback;

    public Collection<HitResult> hit(Collection<AbstractEnemy> enemies, float mul) {
        return this.hit(this.source, enemies, mul, MultiplierStat.ATK, 0, this.source.elementType, false);
    }

    public Collection<HitResult> hit(Collection<AbstractEnemy> enemies, float mul, float toughness) {
        return this.hit(this.source, enemies, mul, MultiplierStat.ATK, toughness, this.source.elementType, false);
    }

    public Collection<HitResult> hit(Collection<AbstractEnemy> enemies, float mul, MultiplierStat stat, float toughness) {
        return this.hit(this.source, enemies, mul, stat, toughness, this.source.elementType, false);
    }

    public Collection<HitResult> hit(Collection<AbstractEnemy> enemies, float mul, MultiplierStat stat, float toughness, ElementType elementType, boolean ignoreWeakness) {
        return this.hit(this.source, enemies, mul, stat, toughness, elementType, ignoreWeakness);
    }

    public Collection<HitResult> hit(AbstractCharacter<?> source, Collection<AbstractEnemy> enemies, float mul) {
        return this.hit(source, enemies, mul, MultiplierStat.ATK, 0, source.elementType, false);
    }

    public Collection<HitResult> hit(AbstractCharacter<?> source, Collection<AbstractEnemy> enemies, float mul, float toughness) {
        return this.hit(source, enemies, mul, MultiplierStat.ATK, toughness, source.elementType, false);
    }

    public Collection<HitResult> hit(AbstractCharacter<?> source, Collection<AbstractEnemy> enemies, float mul, MultiplierStat stat, float toughness) {
        return this.hit(source, enemies, mul, stat, toughness, source.elementType, false);
    }

    public HitResult hit(AbstractEnemy enemy, float mul) {
        return this.hit(this.source, enemy, mul, MultiplierStat.ATK, 0, this.source.elementType, false);
    }

    public HitResult hit(AbstractEnemy enemy, float mul, float toughness) {
        return this.hit(this.source, enemy, mul, MultiplierStat.ATK, toughness, this.source.elementType, false);
    }

    public HitResult hit(AbstractEnemy enemy, float mul, MultiplierStat stat, float toughness) {
        return this.hit(this.source, enemy, mul, stat, toughness, this.source.elementType, false);
    }

    public HitResult hit(AbstractEnemy enemy, float mul, MultiplierStat stat, float toughness, ElementType elementType, boolean ignoreWeakness) {
        return this.hit(this.source, enemy, mul, stat, toughness, elementType, ignoreWeakness);
    }

    public HitResult hit(AbstractCharacter<?> source, AbstractEnemy enemy, float mul) {
        return this.hit(source, enemy, mul, MultiplierStat.ATK, 0, source.elementType, false);
    }

    public HitResult hit(AbstractCharacter<?> source, AbstractEnemy enemy, float mul, float toughness) {
        return this.hit(source, enemy, mul, MultiplierStat.ATK, toughness, source.elementType, false);
    }

    public HitResult hit(AbstractCharacter<?> source, AbstractEnemy enemy, float mul, MultiplierStat stat, float toughness) {
        return this.hit(source, enemy, mul, stat, toughness, source.elementType, false);
    }


    public Collection<HitResult> hit(AbstractCharacter<?> source, Collection<AbstractEnemy> targets, float mul, MultiplierStat stat, float toughness, ElementType elementType, boolean ignoreWeakness) {
        Collection<HitResult> results = new ArrayList<>();
        for (var target : targets) {
            var res = this.hit(source, target, mul, stat, toughness, elementType, ignoreWeakness);
            results.add(res);
        }
        return results;
    }

    public HitResult hit(AbstractCharacter<?> source, AbstractEnemy target, float mul, MultiplierStat stat, float toughness, ElementType elementType, boolean ignoreWeakness) {
        return this.hit(source, target, mul, stat, toughness, elementType, ignoreWeakness, this.types.stream().toList());
    }

    public HitResult hit(AbstractCharacter<?> source, AbstractEnemy target, float mul, MultiplierStat stat, float toughness, ElementType elementType, boolean ignoreWeakness, List<DamageType> types) {
        if (!this.targets.contains(target)) {
            // This is allowed for fixed dmg
            throw new IllegalStateException("Cannot hit target that isn't part of Attack");
        }

        return this.addHit(new AllyHit(source, target, mul, stat, types, toughness, elementType, ignoreWeakness));
    }

    private HitResult hitFixed(AbstractEnemy target, float dmg) {
        return this.hitFixed(this.source, target, dmg);
    }

    public HitResult hitFixed(BattleParticipant source, AbstractEnemy target, float dmg) {
        this.targets.add(target);
        return this.addHit(new FixedHit(source, target, dmg));
    }


    private HitResult addHit(Hit hit) {
        return this.callback.apply(hit);
    }

}
